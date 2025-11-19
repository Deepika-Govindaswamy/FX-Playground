package com.forex.backend.wallet_management_service.dto;

import com.forex.backend.wallet_management_service.utils.Currency;
import lombok.Builder;

// This dto is responsible to collect all details required to execute the payment

@Builder
public record TransactionInitiateDTO (Integer userId, Integer walletId, Double amount, Currency currency, String paymentMethodId, String idempotencyKey) {}

//user id and wallet id - comes from the local storage
// amount and currency from the frontend UI
