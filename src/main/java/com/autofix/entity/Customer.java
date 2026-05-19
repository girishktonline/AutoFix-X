package com.autofix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    
    @Id
    private Long id;
    
    @OneToOne(optional = false)
    @PrimaryKeyJoinColumn
    private User user;
    
    private String address;
    private String city;
    private Double latitude;
    private Double longitude;
    @Builder.Default
    private Double totalSpent = 0.0;
    @Builder.Default
    private Double averageRating = 0.0;
    @Builder.Default
    private Integer totalBookings = 0;
}
