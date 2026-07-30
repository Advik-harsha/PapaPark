package com.parking.system.controller;

import com.parking.system.dto.ParkingSessionDto;
import com.parking.system.dto.ParkingSlotDto;
import com.parking.system.enums.VehicleType;
import com.parking.system.service.ParkingService;
import com.parking.system.service.ParkingSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private ParkingSlotService parkingSlotService;

    @PostMapping("/start")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ParkingSessionDto> startParkingSession(@RequestParam String vehicleId,
                                                                  @RequestParam String slotId) {
        ParkingSessionDto session = parkingService.startParkingSession(vehicleId, slotId);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/end/{sessionId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ParkingSessionDto> endParkingSession(@PathVariable String sessionId) {
        ParkingSessionDto session = parkingService.endParkingSession(sessionId);
        return ResponseEntity.ok(session);
    }

    @GetMapping("/session/{sessionId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ParkingSessionDto> getSessionById(@PathVariable String sessionId) {
        ParkingSessionDto session = parkingService.getSessionById(sessionId);
        return ResponseEntity.ok(session);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ParkingSessionDto>> getActiveSessions() {
        List<ParkingSessionDto> sessions = parkingService.getActiveSessions();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<ParkingSessionDto>> getUserSessions(@PathVariable String userId) {
        List<ParkingSessionDto> sessions = parkingService.getUserSessions(userId);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/has-dues/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Boolean> hasPendingDues(@PathVariable String userId) {
        boolean hasDues = parkingService.hasPendingDues(userId);
        return ResponseEntity.ok(hasDues);
    }

    // Public / User accessible slot endpoints to prevent 403 Forbidden
    @GetMapping("/slots/all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<ParkingSlotDto>> getAllSlots() {
        List<ParkingSlotDto> slots = parkingSlotService.getAllSlots();
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/slots/available")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ParkingSlotDto> getNearestAvailableSlot(@RequestParam VehicleType vehicleType) {
        ParkingSlotDto slot = parkingSlotService.getNearestAvailableSlot(vehicleType);
        return ResponseEntity.ok(slot);
    }
}
