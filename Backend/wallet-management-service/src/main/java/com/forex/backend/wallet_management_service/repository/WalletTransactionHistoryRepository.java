package com.forex.backend.wallet_management_service.repository;

import com.forex.backend.wallet_management_service.entity.WalletTransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionHistoryRepository extends JpaRepository<WalletTransactionHistory,Integer> {
}
