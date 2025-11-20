package com.forex.backend.payment_service.dto;

import lombok.Builder;

// This dto sends required data to initiate the creation of intent in stripe and execute transaction
@Builder
public record TransactionExecutionDTO (Long amount, String paymentMethodId, String currency,
                                       String customerEmail, String transactionId) {
}
