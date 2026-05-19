package com.autofix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "mechanic_earnings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MechanicEarnings {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "mechanic_id", nullable = false)
    private Mechanic mechanic;
    
    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private ServiceBooking booking;
    
    private Double mechanicEarnings; // 70% of service cost
    private Double platformCommission; // 30% of service cost
    private Double totalAmount;
    
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        totalAmount = mechanicEarnings;
    }
}
