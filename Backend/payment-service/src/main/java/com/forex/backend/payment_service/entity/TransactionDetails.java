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

    @NotBlank
    @Email
    private String customerEmail;

    private String customerName;

    private String customerAddress;

    private String stripePaymentIntentId;

    private Long amount;

    private String currency;

    private String paymentMethodId;

    private String idempotencyKey;

    private String status;

    private String failureReason;

    private Instant updatedAt;

    private Instant createdAt;
}
