package com.forex.backend.payment_service.controller;

import com.forex.backend.payment_service.dto.PaymentRequestDTO;
import com.forex.backend.payment_service.dto.PaymentResponseDTO;
import com.forex.backend.payment_service.service.PaymentExecutionService;
import com.forex.backend.payment_service.service.RequestValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment-service")
public class PaymentController {

    @Autowired
    private PaymentExecutionService paymentService;

    @Autowired
    private RequestValidationService requestValidationService;

    @PostMapping("/initiate-payment")
    public ResponseEntity<PaymentResponseDTO> initiatePayment (@RequestBody PaymentRequestDTO paymentRequestDto) {

        String validationResponse = requestValidationService.validateRequest(paymentRequestDto);

        if (!validationResponse.equals("Valid")) {
            var responseBody = new PaymentResponseDTO(null, "Invalid Request: " + validationResponse, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }

        return paymentService.initiateTransaction(paymentRequestDto);

    }
}
