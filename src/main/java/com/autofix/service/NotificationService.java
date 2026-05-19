package com.autofix.service;

import com.autofix.entity.Customer;
import com.autofix.entity.Mechanic;
import com.autofix.entity.ServiceBooking;
import com.autofix.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {
    
    // TODO: Integrate with Twilio for SMS
    // TODO: Integrate with Firebase for push notifications
    // TODO: Integrate with JavaMail for email
    
    // Notify mechanic of new job
    public void notifyMechanicNewJob(Mechanic mechanic, ServiceBooking booking) {
        User user = mechanic.getUser();
        log.info("NOTIFICATION: Mechanic {} - New job available at {},{} | Service: {} | ETA: {} mins | Cost: ₹{}",
                user.getName(), 
                booking.getCustomerLatitude(), 
                booking.getCustomerLongitude(),
                booking.getServiceTypes(),
                booking.getEtaMinutes(),
                booking.getEstimatedCost());
        
        // SMS: "+91" + user.getPhone() - "New job! Distance: " + booking.getDistanceKm() + "km, ETA: " + booking.getEtaMinutes() + "min, ₹" + booking.getEstimatedCost()
        // Push: title="New Job Available", body="Service: " + booking.getServiceTypes()
    }
    
    // Notify customer booking is confirmed
    public void notifyCustomerBookingConfirmed(Customer customer, ServiceBooking booking) {
        User user = customer.getUser();
        log.info("NOTIFICATION: Customer {} - Booking confirmed | Mechanic: {} | ETA: {} mins | Cost: ₹{}",
                user.getName(),
                booking.getMechanic().getUser().getName(),
                booking.getEtaMinutes(),
                booking.getEstimatedCost());
        
        // Email + SMS + Push
        // "Your booking is confirmed. Mechanic " + booking.getMechanic().getUser().getName() + " is on the way. ETA: " + booking.getEtaMinutes() + " minutes"
    }
    
    // Notify customer mechanic accepted job (arriving soon)
    public void notifyCustomerMechanicAccepted(Customer customer, ServiceBooking booking) {
        User user = customer.getUser();
        log.info("NOTIFICATION: Customer {} - Mechanic {} accepted job | Location: {},{} | ETA: {} mins",
                user.getName(),
                booking.getMechanic().getUser().getName(),
                booking.getMechanicLatitude(),
                booking.getMechanicLongitude(),
                booking.getEtaMinutes());
        
        // SMS + Push: "Mechanic " + booking.getMechanic().getUser().getName() + " is on the way! ETA: " + booking.getEtaMinutes() + " mins"
    }
    
    // Notify customer job is completed
    public void notifyCustomerJobCompleted(Customer customer, ServiceBooking booking) {
        User user = customer.getUser();
        log.info("NOTIFICATION: Customer {} - Job completed by {} | Cost: ₹{} | Please rate your experience",
                user.getName(),
                booking.getMechanic().getUser().getName(),
                booking.getActualCost());
        
        // SMS + Push + Email: "Job completed! Please pay ₹" + booking.getActualCost() + " and rate your mechanic"
    }
    
    // Send SMS
    private void sendSMS(String phoneNumber, String message) {
        // TODO: Twilio integration
        log.debug("SMS to {}: {}", phoneNumber, message);
    }
    
    // Send push notification
    private void sendPushNotification(String deviceToken, String title, String body) {
        // TODO: Firebase integration
        log.debug("Push: {} - {}", title, body);
    }
    
    // Send email
    private void sendEmail(String email, String subject, String body) {
        // TODO: JavaMail integration
        log.debug("Email to {}: {}", email, subject);
    }
}
