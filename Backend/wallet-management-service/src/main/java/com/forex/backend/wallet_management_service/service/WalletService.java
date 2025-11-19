package com.forex.backend.wallet_management_service.service;

import com.forex.backend.wallet_management_service.entity.WalletDetails;
import com.forex.backend.wallet_management_service.repository.WalletDetailsRepository;
import com.forex.backend.wallet_management_service.utils.Currency;
import com.forex.backend.wallet_management_service.utils.WalletStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletService {


    private final WalletDetailsRepository walletDetailsRepository;

    public HttpStatus createNewWallet(Integer userId) {

        // Check if wallet already exists
        if (walletDetailsRepository.findByUserId(userId).isPresent()) {
            return  HttpStatus.CONFLICT;
        }

        // Create new wallet and add the initial balance to it
        WalletDetails walletDetails = WalletDetails.builder()
                .userId(userId)
                .activationStatus(WalletStatus.Unfrozen)
                .eurBalance(0.0)
                .gbpBalance(0.0)
                .usdBalance(0.0)
                .build();

        // Persist changes to database
        walletDetailsRepository.save(walletDetails);

        return  HttpStatus.CREATED;
    }

    // transactional annotation avoids race condition
    @Transactional
    public ResponseEntity<String> updateWalletBalance (Integer userId, Integer walletId, Double amount, Currency currency) {

        WalletDetails wallet = walletDetailsRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        // Validate wallet ownership corresponding to userid
        if (!wallet.getUserId().equals(userId)) {
            return  ResponseEntity.badRequest().body("Wallet does not belong to this user");
        }

        // Validate wallet status - freeze or unfreeze before updation
        if (wallet.getActivationStatus().equals(WalletStatus.Frozen)) {
            return ResponseEntity.badRequest().body("Wallet is Frozen");
        }

        // Validate amount value
        if (amount == null || amount <= 0 ) {
            return ResponseEntity.badRequest().body("Amount must be greater than 0");
        }



        switch (currency) {

            case USD -> wallet.setUsdBalance(wallet.getUsdBalance() + amount);

            case EUR -> wallet.setEurBalance(wallet.getEurBalance() + amount);

            case GBP -> wallet.setGbpBalance(wallet.getGbpBalance() + amount);

            default -> {
                return ResponseEntity.badRequest().body("Unsupported currency");
            }
        }

        walletDetailsRepository.save(wallet);

        return ResponseEntity.ok().body("Wallet Balance Updated  Successfully");
    }
}
