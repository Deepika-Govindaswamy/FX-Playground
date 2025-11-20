package com.forex.backend.wallet_management_service.dto;

import lombok.Builder;

@Builder
public record PaymentVerificationResponseDTO(Integer userId, Integer walletId, Long amount, String currency,
                                             String transactionId, String paymentStatus, String clientSecret) {}
