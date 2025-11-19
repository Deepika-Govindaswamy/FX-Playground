package com.forex.backend.wallet_management_service.controller;

import com.forex.backend.wallet_management_service.dto.PaymentRequestDTO;
import com.forex.backend.wallet_management_service.dto.PaymentVerficationResponseDTO;
import com.forex.backend.wallet_management_service.dto.TransactionInitiateDTO;
import com.forex.backend.wallet_management_service.feign.WalletManagement_FeignInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.forex.backend.wallet_management_service.service.WalletService;

@RestController
@RequestMapping("/wallet-service")
@RequiredArgsConstructor
public class WalletManagementController {

    private final WalletService walletService;

    private final WalletManagement_FeignInterface feignInterface;


    // Create wallet when user is created
    // Pass User-id as Path Variable
    @PostMapping ("/create-wallet/{userId}")
    public HttpStatus createNewWallet(@PathVariable Integer userId) {

        HttpStatus walletCreationResponse = walletService.createNewWallet(userId);

        if (walletCreationResponse == HttpStatus.CREATED) {
            return HttpStatus.CREATED;
        }
        return walletCreationResponse;
    }

    @PostMapping ("/add-wallet-fund")
    public ResponseEntity<String> addWalletFund(@RequestBody TransactionInitiateDTO transactionInitiateDTO) {

        // build the payment request dto to carry required information to the payment service
        PaymentRequestDTO paymentRequestDTO = PaymentRequestDTO.builder()
                .amount(transactionInitiateDTO.amount())
                .currency(transactionInitiateDTO.currency())
                .paymentMethodId(transactionInitiateDTO.paymentMethodId())
                .idempotencyKey(transactionInitiateDTO.idempotencyKey())
                .build();

        // call payment service passing the payment request dto to the initiatePayment method in
        // the payment-service using feign client interface.
        ResponseEntity<PaymentVerficationResponseDTO> paymentVerficationResponse =  feignInterface.initiatePayment(paymentRequestDTO);
        PaymentVerficationResponseDTO paymentBody = paymentVerficationResponse.getBody();

        // Check if payment was successful BEFORE updating balance
        if (paymentBody != null && "SUCCESS".equalsIgnoreCase(paymentBody.paymentStatus())) {

            // Update status in payment service (if needed logic requires confirmation)
            feignInterface.updateStatus(paymentBody);

            // Update Local Wallet Balance
            ResponseEntity walletUpdationAtatus = walletService.updateWalletBalance(transactionInitiateDTO.userId(), transactionInitiateDTO.walletId(), transactionInitiateDTO.amount(), transactionInitiateDTO.currency());

            return walletUpdationAtatus;
        }


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment Failed");

    }

}
