package com.parking.system.service;

import com.parking.system.dto.ParkingSlotDto;
import com.parking.system.enums.VehicleType;

import java.util.List;

public interface ParkingSlotService {
    ParkingSlotDto createSlot(ParkingSlotDto slotDto);
    ParkingSlotDto updateSlot(String id, ParkingSlotDto slotDto);
    void deleteSlot(String id);
    ParkingSlotDto getSlotById(String id);
    List<ParkingSlotDto> getAllSlots();
    List<ParkingSlotDto> getSlotsByZone(String zone);
    ParkingSlotDto getNearestAvailableSlot(VehicleType vehicleType);
}
