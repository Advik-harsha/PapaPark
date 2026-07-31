package com.parking.system.service.impl;

import com.parking.system.dto.PaymentDto;
import com.parking.system.entity.ParkingSession;
import com.parking.system.entity.ParkingSlot;
import com.parking.system.entity.Payment;
import com.parking.system.enums.PaymentStatus;
import com.parking.system.enums.PaymentType;
import com.parking.system.exception.EntityNotFoundException;
import com.parking.system.exception.PaymentException;
import com.parking.system.repository.ParkingSessionRepository;
import com.parking.system.repository.ParkingSlotRepository;
import com.parking.system.repository.PaymentRepository;
import com.parking.system.service.EmailService;
import com.parking.system.service.PaymentService;
import com.parking.system.service.SmsService;
import com.parking.system.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ParkingSessionRepository sessionRepository;

    @Autowired
    private ParkingSlotRepository slotRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    @Override
    public PaymentDto processPayment(String sessionId, String paymentTypeStr) {
        ParkingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found with ID: " + sessionId));

        if (session.getPaymentStatus() == PaymentStatus.PAID) {
            throw new PaymentException("Payment has already been made for this session.");
        }

        if (session.getTotalAmount() == null) {
            throw new PaymentException("Bill has not been generated for this session yet.");
        }

        PaymentType type;
        try {
            type = PaymentType.valueOf(paymentTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new PaymentException("Invalid payment type: " + paymentTypeStr);
        }

        Payment payment = Payment.builder()
                .user(session.getUser())
                .parkingSession(session)
                .amount(session.getTotalAmount())
                .paymentType(type)
                .paymentStatus(PaymentStatus.PENDING)
                .transactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .paymentDate(LocalDateTime.now())
                .build();

        // Ensure parking slot is un-occupied and saved in MongoDB
        if (session.getParkingSlot() != null) {
            ParkingSlot slot = session.getParkingSlot();
            slot.setOccupied(false);
            slotRepository.save(slot);
        }

        if (type == PaymentType.WALLET) {
            try {
                walletService.deductMoney(session.getUser().getId(), session.getTotalAmount(), 
                        "Paid for parking session " + sessionId);
                payment.setPaymentStatus(PaymentStatus.PAID);
                session.setPaymentStatus(PaymentStatus.PAID);
            } catch (Exception e) {
                payment.setPaymentStatus(PaymentStatus.FAILED);
                payment = paymentRepository.save(payment);
                throw new PaymentException("Wallet payment failed: " + e.getMessage());
            }
        } else if (type == PaymentType.PAY_NOW) {
            payment.setPaymentStatus(PaymentStatus.PAID);
            session.setPaymentStatus(PaymentStatus.PAID);
        } else if (type == PaymentType.PAY_LATER || type == PaymentType.POSTPAID) {
            payment.setPaymentStatus(PaymentStatus.PENDING);
            session.setPaymentStatus(PaymentStatus.PENDING);
        }

        sessionRepository.save(session);
        payment = paymentRepository.save(payment);

        // Send Payment Success Notifications via Email & SMS
        if (payment.getPaymentStatus() == PaymentStatus.PAID && session.getUser() != null) {
            emailService.sendPaymentReceipt(session.getUser().getEmail(), payment.getTransactionId(), payment.getAmount().toString());
            smsService.sendPaymentSms(session.getUser().getPhone(), payment.getTransactionId(), payment.getAmount().toString());
        }

        return mapToDto(payment);
    }

    @Override
    public PaymentDto getPaymentDetails(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with ID: " + paymentId));
        return mapToDto(payment);
    }

    @Override
    public List<PaymentDto> getUserPayments(String userId) {
        List<Payment> payments = paymentRepository.findByUserId(userId);
        if (payments == null || payments.isEmpty()) {
            payments = paymentRepository.findAll().stream()
                    .filter(p -> p.getUser() != null && userId.equals(p.getUser().getId()))
                    .collect(Collectors.toList());
        }
        return payments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDto> getPendingPayments() {
        return paymentRepository.findAll().stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.PENDING)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PaymentDto mapToDto(Payment payment) {
        PaymentDto dto = new PaymentDto();
        dto.setId(payment.getId());
        if (payment.getUser() != null) {
            dto.setUserId(payment.getUser().getId());
        }
        if (payment.getParkingSession() != null) {
            dto.setSessionId(payment.getParkingSession().getId());
        }
        dto.setAmount(payment.getAmount());
        dto.setPaymentType(payment.getPaymentType());
        dto.setPaymentStatus(payment.getPaymentStatus());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setTransactionId(payment.getTransactionId());
        return dto;
    }
}
