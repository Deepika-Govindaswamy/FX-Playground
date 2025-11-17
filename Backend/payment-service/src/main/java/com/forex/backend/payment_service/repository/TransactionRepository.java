package com.forex.backend.payment_service.repository;

import com.forex.backend.payment_service.entity.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionDetails, String> {

    boolean existsByIdempotencyKey (String idempotencyKey);
    TransactionDetails findByTransactionId (String transactionId);
}
