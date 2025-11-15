package com.forex.backend.payment_service.dto;

import lombok.Builder;

@Builder
public record TransactionExecutionDTO (Long amount, String paymentMethodId, String idempotencyKey, String currency,
                                       String customerEmail, String transactionId) {
}
