package com.parking.system.repository;

import com.parking.system.entity.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    Optional<Vehicle> findByVehicleNumber(String vehicleNumber);

    @Query("{'user.$id': ?0}")
    List<Vehicle> findByUserId(String userId);

    boolean existsByVehicleNumber(String vehicleNumber);
}
