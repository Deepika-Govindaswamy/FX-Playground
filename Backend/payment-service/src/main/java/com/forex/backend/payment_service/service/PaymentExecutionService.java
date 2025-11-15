
package com.forex.backend.payment_service.service;

import com.forex.backend.payment_service.dto.PaymentRequestDTO;
import com.forex.backend.payment_service.dto.PaymentResponseDTO;
import com.forex.backend.payment_service.dto.TransactionExecutionDTO;
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
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentExecutionService {

    @Value("${stripe.secret-key}")
    private String stripeApiKey;

    @Autowired
    private TransactionRepository transactionRepository;

    public ResponseEntity<PaymentResponseDTO> initiateTransaction(PaymentRequestDTO req) {

        Stripe.apiKey = stripeApiKey;

        String transactionId = UUID.randomUUID().toString();

        TransactionExecutionDTO dto = TransactionExecutionDTO.builder()
                .transactionId(transactionId)
                .amount(req.amount())
                .currency(req.currency())
                .customerEmail(req.customerEmail())
                .paymentMethodId(req.paymentMethodId())   // IMPORTANT: must come from frontend
                .build();

        PaymentIntent intent = createPaymentIntent(dto);

        PaymentResponseDTO response = PaymentResponseDTO.builder()
                .transactionId(transactionId)
                .paymentStatus(intent.getStatus())
                .clientSecret(intent.getClientSecret())
                .build();

        log.info("Payment Intent: " + intent.toString());
        log.info("Payment response: " + response.toString());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Create PaymentIntent — DO NOT CONFIRM HERE.
     * Confirmation must happen on the frontend with Stripe.js
     */
    private PaymentIntent createPaymentIntent(TransactionExecutionDTO paymentDetails) {

        try {
            // (1) Create or reuse a Customer
            Customer customer = Customer.create(
                    CustomerCreateParams.builder()
                            .setEmail(paymentDetails.customerEmail())
                            .build()
            );

            log.info("Customer created: {}", customer.getId());

            // (2) Create PaymentIntent (no manual payment method attachment)
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

            // (3) Create PaymentIntent on Stripe (this sends API request)
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
