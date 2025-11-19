package com.forex.backend.wallet_management_service.entity;

import com.forex.backend.wallet_management_service.utils.WalletStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "wallet_details")
public class WalletDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer walletId;

    private Integer userId;

    @Enumerated(EnumType.STRING)
    private WalletStatus activationStatus;

    @Builder.Default
    @Column(nullable = false)
    private Double gbpBalance = 0.00;

    @Builder.Default
    @Column(nullable = false)
    private Double eurBalance = 0.00;

    @Builder.Default
    @Column(nullable = false)
    private Double usdBalance = 0.00;
}
