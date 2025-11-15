package com.forex.backend.payment_service.dto;

import lombok.Builder;

@Builder
public record PaymentResponseDTO (String transactionId, String paymentStatus, String clientSecret) {}
