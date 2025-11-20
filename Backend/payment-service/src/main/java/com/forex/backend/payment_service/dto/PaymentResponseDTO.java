package com.forex.backend.payment_service.dto;

import lombok.Builder;

@Builder
public record PaymentResponseDTO (Integer userId, Integer walletId, Long amount,
                                  String currency, String transactionId, String paymentStatus) {}
