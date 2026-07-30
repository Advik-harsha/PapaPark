package com.parking.system.service;

import com.parking.system.dto.VehicleDto;

import java.util.List;

public interface VehicleService {
    VehicleDto addVehicle(VehicleDto vehicleDto, String userId);
    VehicleDto updateVehicle(String id, VehicleDto vehicleDto);
    void deleteVehicle(String id);
    VehicleDto getVehicleById(String id);
    List<VehicleDto> getVehiclesByUserId(String userId);
    List<VehicleDto> searchVehicles(String keyword);
}
