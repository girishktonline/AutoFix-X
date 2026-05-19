package com.autofix.service;

import com.autofix.config.TwilioConfig;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwilioSmsService {

    private final TwilioConfig twilioConfig;

    /**
     * Send SMS to a phone number
     */
    public boolean sendSms(String toPhoneNumber, String message) {
        try {
            if (twilioConfig.getPhoneNumber() == null || twilioConfig.getPhoneNumber().isEmpty()) {
                log.warn("⚠️ Twilio not configured - SMS not sent to {}", toPhoneNumber);
                return false;
            }

            Message msg = Message.creator(
                    new PhoneNumber(toPhoneNumber),        // To number
                    new PhoneNumber(twilioConfig.getPhoneNumber()), // From number
                    message                                          // Message body
            ).create();

            log.info("✅ SMS sent successfully: {} to {}", msg.getSid(), toPhoneNumber);
            return true;
        } catch (Exception e) {
            log.error("❌ Failed to send SMS to {}: {}", toPhoneNumber, e.getMessage());
            return false;
        }
    }

    /**
     * Notify mechanic about new job assignment
     */
    public void notifyMechanicNewJob(String mechanicPhone, Long bookingId, Double distance) {
        String message = String.format("🔧 AutoFix: New job assignment! Booking #%d, Distance: %.1f km. Open app to accept.", bookingId, distance);
        sendSms(mechanicPhone, message);
    }

    /**
     * Notify customer that mechanic accepted job
     */
    public void notifyCustomerJobAccepted(String customerPhone, String mechanicName, Integer etaMinutes) {
        String message = String.format("✅ AutoFix: %s accepted your job! ETA: %d minutes.", mechanicName, etaMinutes);
        sendSms(customerPhone, message);
    }

    /**
     * Notify customer that mechanic is on the way
     */
    public void notifyCustomerMechanicOnWay(String customerPhone, String mechanicName, String mechanicPhone) {
        String message = String.format("🚗 AutoFix: %s is on the way! Contact: %s", mechanicName, mechanicPhone);
        sendSms(customerPhone, message);
    }

    /**
     * Notify customer that job is completed
     */
    public void notifyCustomerJobCompleted(String customerPhone, Long bookingId, Double amount) {
        String message = String.format("✅ AutoFix: Job #%d completed! Amount: ₹%.2f. Rate your experience in the app.", bookingId, amount);
        sendSms(customerPhone, message);
    }

    /**
     * Notify customer about payment confirmation
     */
    public void notifyPaymentConfirmation(String customerPhone, Double amount, String transactionId) {
        String message = String.format("💳 AutoFix: Payment confirmed! Amount: ₹%.2f | Txn ID: %s", amount, transactionId);
        sendSms(customerPhone, message);
    }
}
