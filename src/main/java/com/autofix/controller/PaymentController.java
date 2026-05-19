package com.autofix.controller;

import com.autofix.entity.PaymentMethod;
import com.autofix.service.PaymentService;
import com.autofix.service.StripePaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowCredentials = "true")
public class PaymentController {
    private final PaymentService paymentService;
    private final StripePaymentService stripePaymentService;

    @PostMapping("/{paymentId}/process")
    public ResponseEntity<?> processPayment(
            @PathVariable Long paymentId,
            @RequestParam String method,
            @RequestParam String transactionId) {
        try {
            paymentService.processPayment(paymentId, Enum.valueOf(PaymentMethod.class, method), transactionId);
            return ResponseEntity.ok("Payment processed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Process payment via Stripe
     * @param bookingId Booking ID
     * @param amount Amount in rupees (will be converted to paise)
     * @param stripeToken Stripe token from frontend
     */
    @PostMapping("/stripe/process")
    public ResponseEntity<?> processStripePayment(
            @RequestParam Long bookingId,
            @RequestParam Long amount,
            @RequestParam String stripeToken) {
        try {
            // Convert rupees to paise (multiply by 100 for Stripe cents)
            Long amountInCents = amount * 100;
            
            var charge = stripePaymentService.processPayment(bookingId, amountInCents, stripeToken);
            
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "chargeId", charge.getId(),
                    "amount", amount,
                    "currency", "INR",
                    "message", "Payment processed successfully"
            ));
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Stripe payment failed: " + e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Payment error: " + e.getMessage()
            ));
        }
    }

    /**
     * Refund a Stripe payment
     */
    @PostMapping("/stripe/{chargeId}/refund")
    public ResponseEntity<?> refundStripePayment(
            @PathVariable String chargeId,
            @RequestParam(required = false) Long amount) {
        try {
            var refund = stripePaymentService.refundPayment(chargeId, amount);
            
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "refundId", refund.getId(),
                    "message", "Refund processed successfully"
            ));
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Refund failed: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<?> refundPayment(
            @PathVariable Long paymentId,
            @RequestParam String reason) {
        try {
            paymentService.refundPayment(paymentId, reason);
            return ResponseEntity.ok("Payment refunded");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{paymentId}/status")
    public ResponseEntity<?> getPaymentStatus(@PathVariable Long paymentId) {
        try {
            return ResponseEntity.ok(paymentService.getPaymentStatus(paymentId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
