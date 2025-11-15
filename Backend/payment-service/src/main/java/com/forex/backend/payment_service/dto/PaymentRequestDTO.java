package com.forex.backend.payment_service.dto;

import lombok.Builder;

@Builder
public record PaymentRequestDTO (Long amount, String currency, String paymentMethodId,
                                 String idempotencyKey, String customerEmail) {}