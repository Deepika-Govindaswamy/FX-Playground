package com.forex.backend.wallet_management_service.dto;

import com.forex.backend.wallet_management_service.utils.Currency;
import lombok.Builder;

// This DTO will send only the necessary details required by the payment service to initiate transaction.
@Builder
public record PaymentRequestDTO(Double amount, Currency currency,
                                String paymentMethodId, String idempotencyKey) {
}
