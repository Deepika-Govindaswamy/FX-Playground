package com.forex.backend.wallet_management_service.controller;

import com.forex.backend.wallet_management_service.dto.PaymentResponseDTO;
import com.forex.backend.wallet_management_service.dto.PaymentVerificationResponseDTO;
import com.forex.backend.wallet_management_service.dto.TransactionInitiateDTO;
import com.forex.backend.wallet_management_service.feign.WalletManagement_FeignInterface;
import com.forex.backend.wallet_management_service.utils.Currency;
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
    public ResponseEntity<PaymentVerificationResponseDTO> addWalletFund(@RequestBody TransactionInitiateDTO transactionInitiateDTO) {

        // call payment service passing the payment request dto to the initiatePayment method in
        // the payment-service using feign client interface.
        ResponseEntity<PaymentVerificationResponseDTO> paymentVerficationRequest =  feignInterface.initiatePayment(transactionInitiateDTO);

        // Send the client transaction details and client secret to the frontend for verfication
        return paymentVerficationRequest;

    }

    /**
     *
     * @param dto - of type PaymentVerificationResponseDTO
     * @return status of updating balance of wallet and saving the wallet transaction to wallet transaction history
     */
    @PostMapping ("/confirm-payment")
    public ResponseEntity<String> confirmPayment(@RequestBody PaymentVerificationResponseDTO dto) {

        // Call the update payment service to save the latest payment status in the database
        ResponseEntity<PaymentResponseDTO> responseDTO = feignInterface.updateStatus(dto);

        PaymentResponseDTO paymentResponseDTOBody = responseDTO.getBody();

        // Update wallet balance corresponding to the user and wallet id
        ResponseEntity<String> walletUpdateResponse = walletService.updateWalletBalance(
                paymentResponseDTOBody.userId(),
                paymentResponseDTOBody.walletId(),
                dto.amount(),
                Currency.valueOf(dto.currency().toUpperCase())
        );

        // Save the status of the transaction made in the wallet
        walletService.saveWalletTransactionHistory(paymentResponseDTOBody, dto.amount(), dto.currency());


        return walletUpdateResponse;
    }



}
