package com.autofix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @Column(nullable = false, unique = true)
    private String vehicleNumber;
    
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType; // CAR, BIKE, TRUCK, AUTO
    
    private String brand;
    private String model;
    private Integer year;
    private String color;
    private LocalDate insuranceExpiry;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

enum VehicleType {
    CAR, BIKE, TRUCK, AUTO
}
