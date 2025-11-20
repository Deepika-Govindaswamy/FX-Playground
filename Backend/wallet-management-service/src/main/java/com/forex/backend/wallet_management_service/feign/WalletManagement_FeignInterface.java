package com.forex.backend.wallet_management_service.feign;

import com.forex.backend.wallet_management_service.dto.TransactionInitiateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.forex.backend.wallet_management_service.dto.PaymentVerificationResponseDTO;
import com.forex.backend.wallet_management_service.dto.PaymentResponseDTO;

@FeignClient (name = "PAYMENT-SERVICE", url = "http://localhost:8080/payment-service")
public interface WalletManagement_FeignInterface {

    @PostMapping("/initiate-payment")
    public ResponseEntity<PaymentVerificationResponseDTO> initiatePayment (@RequestBody TransactionInitiateDTO req);

    @PostMapping("/update-status")
    public ResponseEntity<PaymentResponseDTO> updateStatus(@RequestBody PaymentVerificationResponseDTO dto);

}
