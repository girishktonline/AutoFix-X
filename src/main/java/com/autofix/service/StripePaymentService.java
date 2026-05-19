package com.autofix.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.RefundCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class StripePaymentService {

    @Autowired
    private PaymentService paymentService;

    /**
     * Process payment through Stripe
     * @param bookingId Booking ID for payment
     * @param amount Amount in paise (multiply by 100 for Stripe)
     * @param stripeToken Stripe token from frontend
     * @return Charge object with transaction details
     */
    public Charge processPayment(Long bookingId, Long amount, String stripeToken) throws StripeException {
        try {
            // Amount in paise, Stripe uses cents (multiply by 100)
            Long amountInCents = amount;

            // Create charge
            ChargeCreateParams params = ChargeCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("inr")
                    .setSource(stripeToken)
                    .setDescription("AutoFix Payment for Booking #" + bookingId)
                    .setMetadata(Map.of(
                            "bookingId", bookingId.toString(),
                            "platform", "AutoFix"
                    ))
                    .build();

            Charge charge = Charge.create(params);
            log.info("✅ Stripe Payment Processed: {} - Amount: ₹{} - Status: {}", 
                    charge.getId(), amount / 100.0, charge.getStatus());

            return charge;
        } catch (StripeException e) {
            log.error("❌ Stripe Payment Failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Refund a Stripe charge
     * @param chargeId Stripe charge ID
     * @param amount Amount to refund (optional, null = full refund)
     * @return Refund object
     */
    public Refund refundPayment(String chargeId, Long amount) throws StripeException {
        try {
            RefundCreateParams.Builder paramsBuilder = RefundCreateParams.builder()
                    .setCharge(chargeId);

            if (amount != null) {
                paramsBuilder.setAmount(amount);
            }

            Refund refund = Refund.create(paramsBuilder.build());
            log.info("✅ Stripe Refund Processed: {} - Status: {}", refund.getId(), refund.getStatus());

            return refund;
        } catch (StripeException e) {
            log.error("❌ Stripe Refund Failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieve charge details
     */
    public Charge getChargeDetails(String chargeId) throws StripeException {
        return Charge.retrieve(chargeId);
    }
}
