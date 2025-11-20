package com.forex.backend.payment_service.dto;

import lombok.Builder;

/**
 *
 * @param paymentMethodId
 * @param transactionId
 * @param paymentStatus
 * @param clientSecret
 */

@Builder
public record PaymentVerficationResponseDTO(Integer userId, Integer walletId, Long amount, String currency,
                                            String paymentMethodId, String transactionId, String paymentStatus,
                                            String clientSecret) {

}
