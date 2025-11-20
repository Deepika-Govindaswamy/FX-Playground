package com.forex.backend.wallet_management_service.dto;

import com.forex.backend.wallet_management_service.utils.Currency;
import lombok.Builder;

/** This dto is responsible to collect all details required to execute the payment
 *
 * @param userId
 * @param walletId
 * @param amount
 * @param currency
 * @param paymentMethodId
 * @param idempotencyKey
 */
@Builder
public record TransactionInitiateDTO (Integer userId, Integer walletId, Long amount, Currency currency,
                                      String paymentMethodId, String idempotencyKey) {}

// user id and wallet id - comes from the browser storage
// paymentMethodId, idempotencyKey, amount and currency from the frontend UI
