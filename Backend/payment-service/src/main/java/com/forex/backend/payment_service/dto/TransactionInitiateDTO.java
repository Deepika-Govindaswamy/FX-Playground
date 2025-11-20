package com.forex.backend.payment_service.dto;

import lombok.Builder;

/**
 *
 * @param userId
 * @param walletId
 * @param amount
 * @param currency
 * @param paymentMethodId
 * @param idempotencyKey
 */

@Builder
public record TransactionInitiateDTO (Integer userId, Integer walletId, Long amount, String currency,
                                     String paymentMethodId, String idempotencyKey) {}

// amount, currency - Comes from wallet-management-service
// paymentMethodId, idempotencyKey Comes from the frontend