package com.parking.system.repository;

import com.parking.system.entity.ParkingSession;
import com.parking.system.enums.ParkingStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSessionRepository extends MongoRepository<ParkingSession, String> {
    @Query("{'user.$id': ?0}")
    List<ParkingSession> findByUserId(String userId);

    @Query("{'vehicle.$id': ?0}")
    List<ParkingSession> findByVehicleId(String vehicleId);

    @Query("{'vehicle.$id': ?0, 'status': ?1}")
    Optional<ParkingSession> findByVehicleIdAndStatus(String vehicleId, ParkingStatus status);
}
