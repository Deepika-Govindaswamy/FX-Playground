package com.forex.backend.wallet_management_service.dto;

public record PaymentResponseDTO (Integer userId, Integer walletId, String transactionId, String paymentStatus) {
}
