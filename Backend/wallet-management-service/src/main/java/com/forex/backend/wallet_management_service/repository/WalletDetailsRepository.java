package com.forex.backend.wallet_management_service.repository;

import com.forex.backend.wallet_management_service.entity.WalletDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Keeps track of users and their wallets

@Repository
public interface WalletDetailsRepository extends JpaRepository <WalletDetails, Integer> {

    Optional<WalletDetails> findByUserId (Integer userId);

}
