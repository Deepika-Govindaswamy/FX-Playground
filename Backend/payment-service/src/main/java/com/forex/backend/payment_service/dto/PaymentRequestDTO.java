package com.forex.backend.payment_service.dto;

import lombok.Builder;

@Builder
public record PaymentRequestDTO (Double amount, String currency,
                                 String paymentMethodId, String idempotencyKey) {}

// amount, currency - Comes from wallet-management-service
// paymentMethodId, idempotencyKey Comes from the frontend