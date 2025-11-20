
package com.forex.backend.payment_service.service;

import com.forex.backend.payment_service.dto.TransactionInitiateDTO;
import com.forex.backend.payment_service.dto.PaymentVerficationResponseDTO;
import com.forex.backend.payment_service.dto.TransactionExecutionDTO;
import com.forex.backend.payment_service.entity.TransactionDetails;
import com.forex.backend.payment_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentExecutionService {

    @Value("${stripe.secret-key}")
    private String stripeApiKey;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     *
     * @param req - type TransactionInitiateDTO
     * @return ResponseEntity<PaymentVerficationResponseDTO>
     */
    public ResponseEntity<PaymentVerficationResponseDTO> initiateTransaction(TransactionInitiateDTO req) {

        Stripe.apiKey = stripeApiKey;

        String transactionId = UUID.randomUUID().toString();

        TransactionExecutionDTO transactionDto = TransactionExecutionDTO.builder()
                .transactionId(transactionId)
                .amount(req.amount())
                .currency(req.currency())
                .paymentMethodId(req.paymentMethodId())
                .build();

        PaymentIntent intent = createPaymentIntent(transactionDto);

        PaymentVerficationResponseDTO response = PaymentVerficationResponseDTO.builder()
                .userId(req.userId())
                .walletId(req.walletId())
                .amount(req.amount())
                .currency(req.currency())
                .paymentMethodId(req.paymentMethodId())
                .transactionId(transactionId)
                .paymentStatus(intent.getStatus())
                .clientSecret(intent.getClientSecret())
                .build();

        log.info("Payment Intent: {}", intent.getLastPaymentError());

        // Save transaction details into database
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .transactionId(transactionId)
                .stripePaymentIntentId(intent.getId())
                .amount(req.amount()/100.0)
                .currency(req.currency())
                .paymentMethodId(req.paymentMethodId())
                .idempotencyKey(req.idempotencyKey())
                .status(intent.getStatus())
                .createdAt(Instant.now())
                .userId(req.userId())
                .walletId(req.walletId())
                .build();

        transactionRepository.save(transactionDetails);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Create PaymentIntent
     */
    private PaymentIntent createPaymentIntent(TransactionExecutionDTO paymentDetails) {

        try {
            // Create a Customer
            Customer customer = Customer.create(
                    CustomerCreateParams.builder()
                            .setEmail(paymentDetails.customerEmail())
                            .build()
            );

            log.info("Customer created: {}", customer.getId());

            // Create PaymentIntent
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(paymentDetails.amount())
                    .setCurrency(paymentDetails.currency())
                    .setCustomer(customer.getId())
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();

            // Create PaymentIntent on Stripe
            PaymentIntent intent = PaymentIntent.create(
                    params,
                    RequestOptions.builder()
                            .setIdempotencyKey(paymentDetails.transactionId())
                            .build()
            );

            log.info("PaymentIntent created: {}", intent.getId());
            return intent;

        } catch (StripeException e) {
            log.error("Stripe error while creating PaymentIntent: {}", e.getMessage());
            throw new RuntimeException("Payment processing failed", e);
        }
    }
}
