package com.forex.backend.payment_service.service;

import com.forex.backend.payment_service.dto.TransactionInitiateDTO;
import com.forex.backend.payment_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestValidationService {

    @Autowired
    private final TransactionRepository transactionRepository;

    public String validateRequest (TransactionInitiateDTO transactionInitiateDto) {

        Long amount = transactionInitiateDto.amount();
        String idempotencyKey = transactionInitiateDto.idempotencyKey();
        String paymentMethodId = transactionInitiateDto.paymentMethodId();

        if (amount == null || amount <= 0 || amount > 99999999){
            return "Enter a valid amount";
        }

        if (idempotencyKey == null || idempotencyKey.isEmpty()){
            return "Idempotency key is empty";
        }

        boolean foundIdempotencyKey = transactionRepository.existsByIdempotencyKey(idempotencyKey);
        if (foundIdempotencyKey){
            return "Duplicate request. PaymentTransaction with same idempotency key already processed.";
        }

        if (paymentMethodId == null || !paymentMethodId.startsWith("pm_")) {
            return "Invalid payment method id";
        }

        return "Valid";
    }

}
