package com.forex.backend.payment_service.controller;

import com.forex.backend.payment_service.dto.PaymentRequestDTO;
import com.forex.backend.payment_service.dto.PaymentResponseDTO;
import com.forex.backend.payment_service.entity.TransactionDetails;
import com.forex.backend.payment_service.repository.TransactionRepository;
import com.forex.backend.payment_service.service.PaymentExecutionService;
import com.forex.backend.payment_service.service.RequestValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment-service")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentExecutionService paymentService;

    @Autowired
    private RequestValidationService requestValidationService;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/initiate-payment")
    public ResponseEntity<PaymentResponseDTO> initiatePayment (@RequestBody PaymentRequestDTO paymentRequestDto) {

        String validationResponse = requestValidationService.validateRequest(paymentRequestDto);

        if (!validationResponse.equals("Valid")) {
            var responseBody = new PaymentResponseDTO(null, "Invalid Request: " + validationResponse, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }

        return paymentService.initiateTransaction(paymentRequestDto);

    }

    @PostMapping("/update-status")
    public ResponseEntity<Void> updateStatus(@RequestBody PaymentResponseDTO dto) {
        TransactionDetails txDetails = transactionRepository.findByTransactionId(dto.transactionId());
        txDetails.setStatus(dto.paymentStatus());
        transactionRepository.save(txDetails);
        log.info("Transaction with id {} updated successfully", dto.paymentStatus());
        return ResponseEntity.ok().build();
    }

}
