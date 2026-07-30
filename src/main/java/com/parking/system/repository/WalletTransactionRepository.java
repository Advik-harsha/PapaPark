package com.parking.system.repository;

import com.parking.system.entity.WalletTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionRepository extends MongoRepository<WalletTransaction, String> {
    @Query(value = "{'user.$id': ?0}", sort = "{'transactionDate': -1}")
    List<WalletTransaction> findByUserIdOrderByTransactionDateDesc(String userId);
}
