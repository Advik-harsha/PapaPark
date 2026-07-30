package com.parking.system.repository;

import com.parking.system.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    @Query("{'user.$id': ?0}")
    List<Payment> findByUserId(String userId);

    Optional<Payment> findByTransactionId(String transactionId);

    @Query("{'parkingSession.$id': ?0}")
    Optional<Payment> findByParkingSessionId(String sessionId);
}
