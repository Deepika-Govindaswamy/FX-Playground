package com.forex.backend.payment_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "transactions_record")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetails {

    @Id
    private String transactionId;

    private String stripePaymentIntentId;

    private Double amount;

    private String currency;

    private String paymentMethodId;

    private String idempotencyKey;

    private String status;

    private Instant createdAt;

    private Integer userId;

    private Integer walletId;
}
