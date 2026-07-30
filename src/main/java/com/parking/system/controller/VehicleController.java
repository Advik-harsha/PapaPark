package com.parking.system.controller;

import com.parking.system.dto.VehicleDto;
import com.parking.system.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<VehicleDto> addVehicle(@Valid @RequestBody VehicleDto vehicleDto,
                                                  @RequestParam String userId) {
        VehicleDto vehicle = vehicleService.addVehicle(vehicleDto, userId);
        return ResponseEntity.ok(vehicle);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<VehicleDto> updateVehicle(@PathVariable String id,
                                                     @Valid @RequestBody VehicleDto vehicleDto) {
        VehicleDto vehicle = vehicleService.updateVehicle(id, vehicleDto);
        return ResponseEntity.ok(vehicle);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> deleteVehicle(@PathVariable String id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok("Vehicle deleted successfully");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<VehicleDto> getVehicleById(@PathVariable String id) {
        VehicleDto vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<VehicleDto>> getVehiclesByUserId(@PathVariable String userId) {
        List<VehicleDto> vehicles = vehicleService.getVehiclesByUserId(userId);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<VehicleDto>> searchVehicles(@RequestParam String keyword) {
        List<VehicleDto> vehicles = vehicleService.searchVehicles(keyword);
        return ResponseEntity.ok(vehicles);
    }
}
