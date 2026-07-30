package com.parking.system.service.impl;

import com.parking.system.dto.ParkingSessionDto;
import com.parking.system.entity.ParkingSession;
import com.parking.system.entity.ParkingSlot;
import com.parking.system.entity.User;
import com.parking.system.entity.Vehicle;
import com.parking.system.enums.ParkingStatus;
import com.parking.system.enums.PaymentStatus;
import com.parking.system.exception.EntityNotFoundException;
import com.parking.system.exception.ParkingException;
import com.parking.system.repository.ParkingSessionRepository;
import com.parking.system.repository.ParkingSlotRepository;
import com.parking.system.repository.UserRepository;
import com.parking.system.repository.VehicleRepository;
import com.parking.system.service.EmailService;
import com.parking.system.service.ParkingService;
import com.parking.system.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingServiceImpl implements ParkingService {

    @Autowired
    private ParkingSessionRepository sessionRepository;

    @Autowired
    private ParkingSlotRepository slotRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    private static final BigDecimal FIRST_HOUR_RATE = new BigDecimal("30");
    private static final BigDecimal ADDITIONAL_HOUR_RATE = new BigDecimal("20");

    @Override
    public ParkingSessionDto startParkingSession(String vehicleId, String slotId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with ID: " + vehicleId));

        User user = vehicle.getUser();

        // Prevent duplicate active parking
        Optional<ParkingSession> activeSession = sessionRepository.findByVehicleIdAndStatus(vehicleId, ParkingStatus.ACTIVE);
        if (activeSession.isPresent()) {
            throw new ParkingException("This vehicle already has an active parking session!");
        }

        // Mandatory check for unpaid Postpaid/Pay-Later dues before starting new session
        if (user != null && hasPendingDues(user.getId())) {
            throw new ParkingException("🚫 Mandatory Payment Required! You have unpaid Postpaid dues from a previous session. Please settle your outstanding dues before starting a new parking session!");
        }

        ParkingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new EntityNotFoundException("Slot not found with ID: " + slotId));

        if (slot.isOccupied()) {
            throw new ParkingException("Selected slot is already occupied.");
        }

        if (slot.getVehicleType() != vehicle.getVehicleType()) {
            throw new ParkingException("Slot type does not match vehicle type.");
        }

        slot.setOccupied(true);
        slotRepository.save(slot);

        ParkingSession session = ParkingSession.builder()
                .user(user)
                .vehicle(vehicle)
                .parkingSlot(slot)
                .entryTime(LocalDateTime.now())
                .status(ParkingStatus.ACTIVE)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        session = sessionRepository.save(session);

        // Send Email & SMS notification
        if (user != null) {
            emailService.sendBookingConfirmation(user.getEmail(), vehicle.getVehicleNumber(), slot.getSlotNumber());
            smsService.sendBookingSms(user.getPhone(), vehicle.getVehicleNumber(), slot.getSlotNumber());
        }

        return mapToDto(session);
    }

    @Override
    public ParkingSessionDto endParkingSession(String sessionId) {
        ParkingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found with ID: " + sessionId));

        if (session.getStatus() == ParkingStatus.COMPLETED) {
            throw new ParkingException("Session is already completed.");
        }

        session.setExitTime(LocalDateTime.now());
        session.setStatus(ParkingStatus.COMPLETED);

        // Calculate hours
        long minutes = Duration.between(session.getEntryTime(), session.getExitTime()).toMinutes();
        int hours = (int) Math.ceil(minutes / 60.0);
        if (hours == 0) hours = 1; // Minimum 1 hour
        session.setTotalHours(hours);

        // Calculate Amount
        BigDecimal totalAmount = FIRST_HOUR_RATE;
        if (hours > 1) {
            BigDecimal additionalAmount = ADDITIONAL_HOUR_RATE.multiply(new BigDecimal(hours - 1));
            totalAmount = totalAmount.add(additionalAmount);
        }
        session.setTotalAmount(totalAmount);

        session = sessionRepository.save(session);
        return mapToDto(session);
    }

    @Override
    public ParkingSessionDto getSessionById(String sessionId) {
        ParkingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found with ID: " + sessionId));
        return mapToDto(session);
    }

    @Override
    public List<ParkingSessionDto> getActiveSessions() {
        return sessionRepository.findAll().stream()
                .filter(s -> s.getStatus() == ParkingStatus.ACTIVE)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParkingSessionDto> getUserSessions(String userId) {
        List<ParkingSession> sessions = sessionRepository.findByUserId(userId);
        if (sessions == null || sessions.isEmpty()) {
            sessions = sessionRepository.findAll().stream()
                    .filter(s -> s.getUser() != null && userId.equals(s.getUser().getId()))
                    .collect(Collectors.toList());
        }
        return sessions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasPendingDues(String userId) {
        // Returns true if user has any COMPLETED parking session with PENDING payment status (Postpaid dues)
        return getUserSessions(userId).stream()
                .anyMatch(s -> s.getPaymentStatus() == PaymentStatus.PENDING 
                        && s.getStatus() == ParkingStatus.COMPLETED);
    }

    private ParkingSessionDto mapToDto(ParkingSession session) {
        ParkingSessionDto dto = new ParkingSessionDto();
        dto.setId(session.getId());
        if (session.getUser() != null) {
            dto.setUserId(session.getUser().getId());
            dto.setUserName(session.getUser().getFullName());
        }
        if (session.getVehicle() != null) {
            dto.setVehicleId(session.getVehicle().getId());
            dto.setVehicleNumber(session.getVehicle().getVehicleNumber());
        }
        if (session.getParkingSlot() != null) {
            dto.setSlotId(session.getParkingSlot().getId());
            dto.setSlotNumber(session.getParkingSlot().getSlotNumber());
        }
        dto.setEntryTime(session.getEntryTime());
        dto.setExitTime(session.getExitTime());
        dto.setTotalHours(session.getTotalHours());
        dto.setTotalAmount(session.getTotalAmount());
        dto.setStatus(session.getStatus());
        dto.setPaymentStatus(session.getPaymentStatus());
        return dto;
    }
}
