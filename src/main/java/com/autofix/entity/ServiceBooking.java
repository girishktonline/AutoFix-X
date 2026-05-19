package com.autofix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceBooking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
    
    @ManyToOne
    @JoinColumn(name = "mechanic_id")
    private Mechanic mechanic;
    
    @Column(columnDefinition = "TEXT")
    private String serviceTypes; // Comma-separated: PUNCTURE,WASH,BRAKE
    
    @Column(columnDefinition = "TEXT")
    private String symptoms;
    
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;
    
    private LocalDateTime bookingTime;
    private LocalDateTime scheduledTime;
    private LocalDateTime arrivalTime;
    private LocalDateTime completionTime;
    
    private Double estimatedCost;
    private Double actualCost;
    
    private Double customerLatitude;
    private Double customerLongitude;
    
    private Double mechanicLatitude;
    private Double mechanicLongitude;
    
    private Double distanceKm;
    private Integer etaMinutes;
    
    private Integer customerRating;
    private Integer mechanicRating;
    
    @Column(columnDefinition = "TEXT")
    private String customerReview;
    
    @Column(columnDefinition = "TEXT")
    private String mechanicReview;
    
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    private String paymentMethod;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        bookingTime = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
