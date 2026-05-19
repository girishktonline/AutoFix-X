package com.autofix.service;

import com.autofix.entity.*;
import com.autofix.repository.MechanicEarningsRepository;
import com.autofix.repository.PaymentRepository;
import com.autofix.repository.ServiceBookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final MechanicEarningsRepository mechanicEarningsRepository;
    private final ServiceBookingRepository bookingRepository;

    // Commission split: 70% to mechanic, 30% to platform
    private static final double MECHANIC_COMMISSION = 0.70;
    private static final double PLATFORM_COMMISSION = 0.30;

    // Create payment record for completed booking
    public Payment createPaymentForBooking(ServiceBooking booking) {
        double amount = booking.getActualCost();
        
        Payment payment = Payment.builder()
                .serviceBooking(booking)
                .amount(amount)
                .paymentMethod(PaymentMethod.PENDING)
                .transactionId("TXN_" + System.currentTimeMillis())
                .status(PaymentStatus.PENDING)
                .build();

        payment = paymentRepository.save(payment);
        log.info("Payment created for booking {}: ₹{}", booking.getId(), amount);

        // Create mechanic earnings record (70% cut)
        double mechanicEarnings = amount * MECHANIC_COMMISSION;
        double platformCommission = amount * PLATFORM_COMMISSION;

        MechanicEarnings earnings = MechanicEarnings.builder()
                .mechanic(booking.getMechanic())
                .booking(booking)
                .mechanicEarnings(mechanicEarnings)
                .platformCommission(platformCommission)
                .totalAmount(amount)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        mechanicEarningsRepository.save(earnings);
        log.info("Mechanic earnings created: ₹{} for mechanic {}, platform gets ₹{}", 
                mechanicEarnings, booking.getMechanic().getUser().getName(), platformCommission);

        return payment;
    }

    // Process payment (in real app, would integrate with Stripe/Razorpay)
    public void processPayment(Long paymentId, PaymentMethod paymentMethod, String transactionId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // TODO: Integrate with payment gateway
        // For now, mark as completed
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionId(transactionId);
        paymentRepository.save(payment);

        // Mark booking as paid
        ServiceBooking booking = payment.getServiceBooking();
        booking.setPaymentStatus(PaymentStatus.COMPLETED);
        bookingRepository.save(booking);

        // Mark mechanic earnings as completed
        MechanicEarnings earnings = mechanicEarningsRepository.findByBookingId(payment.getServiceBooking().getId())
                .orElseThrow(() -> new RuntimeException("Earnings record not found"));
        earnings.setPaymentStatus(PaymentStatus.COMPLETED);
        mechanicEarningsRepository.save(earnings);

        log.info("Payment {} processed successfully. Amount: ₹{}", paymentId, payment.getAmount());
    }

    // Refund payment
    public void refundPayment(Long paymentId, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new RuntimeException("Can only refund completed payments");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        // Reverse mechanic earnings
        MechanicEarnings earnings = mechanicEarningsRepository.findByBookingId(payment.getServiceBooking().getId())
                .orElseThrow(() -> new RuntimeException("Earnings record not found"));
        earnings.setPaymentStatus(PaymentStatus.REFUNDED);
        mechanicEarningsRepository.save(earnings);

        log.info("Payment {} refunded. Reason: {}", paymentId, reason);
    }

    // Get payment status
    public PaymentStatus getPaymentStatus(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .map(Payment::getStatus)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}
