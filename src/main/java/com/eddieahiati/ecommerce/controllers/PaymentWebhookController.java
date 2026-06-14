package com.eddieahiati.ecommerce.controllers;

import com.eddieahiati.ecommerce.entities.Order;
import com.eddieahiati.ecommerce.entities.OrderStatus;
import com.eddieahiati.ecommerce.repositories.OrderRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.util.HexFormat;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentWebhookController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OrderRepository orderRepository; // Your order repository

    @Value("${paystack.secret}")
    private String paystackSecretKey;

    @PostMapping("/webhook")
    public ResponseEntity<String> handlePaystackWebhook(
            @RequestBody String payload,
            @RequestHeader("x-paystack-signature") String signature
    ) {
        try {
            // 1. Verify signature
            if (!verifySignature(payload, signature)) {
                return ResponseEntity.status(401).body("Invalid signature");
            }

            // 2. Parse payload
            JsonNode json = objectMapper.readTree(payload);
            String event = json.get("event").asText();

            // 3. Handle different event types
            switch (event) {
                case "charge.success":
                    handleChargeSuccess(json);
                    break;
                case "charge.failed":
                    handleChargeFailed(json);
                    break;
                case "charge.dispute.create":
                    handleDisputeCreated(json);
                    break;
                default:
                    // Ignore other events
                    System.out.println("Unhandled event: " + event);
            }

            // 4. Always return 200 to acknowledge receipt
            return ResponseEntity.ok("Webhook processed");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing webhook");
        }
    }

    private boolean verifySignature(String payload, String signatureHeader) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secret = new SecretKeySpec(paystackSecretKey.getBytes(), "HmacSHA512");
            mac.init(secret);
            byte[] hash = mac.doFinal(payload.getBytes());
            String computedSignature = HexFormat.of().formatHex(hash);
            return computedSignature.equals(signatureHeader);
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    private void handleChargeSuccess(JsonNode json) {
        JsonNode data = json.get("data");
        String reference = data.get("reference").asText();
        String amount = data.get("amount").asText(); // in kobo/cents
        String customerEmail = data.get("customer").get("email").asText();

        // Find order by reference (assuming you stored transaction reference when creating payment)
        Order order = orderRepository.findByTransactionReference(reference)
                .orElseThrow(() -> new RuntimeException("Order not found for reference: " + reference));

        // Update order status
        order.setStatus(OrderStatus.PAID);
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);

        System.out.println("Payment successful for order: " + order.getId() + ", reference: " + reference);
    }

    @Transactional
    private void handleChargeFailed(JsonNode json) {
        JsonNode data = json.get("data");
        String reference = data.get("reference").asText();
        String error = data.has("gateway_response") ? data.get("gateway_response").asText() : "Unknown error";

        Order order = orderRepository.findByTransactionReference(reference)
                .orElseThrow(() -> new RuntimeException("Order not found for reference: " + reference));

        order.setStatus(OrderStatus.FAILED);
        orderRepository.save(order);

        System.out.println("Payment failed for reference: " + reference + ", reason: " + error);
    }

    private void handleDisputeCreated(JsonNode json) {
        // Log dispute for manual review
        JsonNode data = json.get("data");
        String reference = data.get("reference").asText();
        System.out.println("Dispute created for transaction: " + reference);
        // Notify admin, update order status, etc.
    }
}