package com.forex.backend.wallet_management_service.entity;

import com.forex.backend.wallet_management_service.utils.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "wallet_transaction_history")
public class WalletTransactionHistory {

    @Id
    private Integer walletId;

    private Integer userId;

    private Double amount;

    private Currency currency;

    private Instant transactionTime;

    private String transactionId;

    private String transactionStatus;
}
