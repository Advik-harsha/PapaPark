package com.parking.system.service;

import com.parking.system.dto.ParkingSessionDto;

import java.util.List;

public interface ParkingService {
    ParkingSessionDto startParkingSession(String vehicleId, String slotId);
    ParkingSessionDto endParkingSession(String sessionId);
    ParkingSessionDto getSessionById(String sessionId);
    List<ParkingSessionDto> getActiveSessions();
    List<ParkingSessionDto> getUserSessions(String userId);
    boolean hasPendingDues(String userId);
}
