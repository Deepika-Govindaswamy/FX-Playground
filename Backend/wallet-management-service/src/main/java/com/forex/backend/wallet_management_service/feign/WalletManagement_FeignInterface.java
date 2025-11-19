package com.forex.backend.wallet_management_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.forex.backend.wallet_management_service.dto.PaymentVerficationResponseDTO;
import com.forex.backend.wallet_management_service.dto.PaymentRequestDTO;
import com.forex.backend.wallet_management_service.dto.PaymentResponseDTO;

@FeignClient (name = "PAYMENT-SERVICE", url = "http://localhost:8080")
public interface WalletManagement_FeignInterface {

    @PostMapping("/initiate-payment")
    public ResponseEntity<PaymentVerficationResponseDTO> initiatePayment (@RequestBody PaymentRequestDTO paymentRequestDto);

    @PostMapping("/update-status")
    public ResponseEntity<PaymentResponseDTO> updateStatus(@RequestBody PaymentVerficationResponseDTO dto);

}
