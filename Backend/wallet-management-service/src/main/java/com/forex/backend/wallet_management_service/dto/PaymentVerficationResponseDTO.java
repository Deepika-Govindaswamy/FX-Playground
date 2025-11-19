package com.forex.backend.wallet_management_service.dto;

import lombok.Builder;

@Builder
public record PaymentVerficationResponseDTO(String transactionId, String paymentStatus, String clientSecret) {}
