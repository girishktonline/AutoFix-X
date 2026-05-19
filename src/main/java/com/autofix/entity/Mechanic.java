package com.autofix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mechanics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mechanic {
    
    @Id
    private Long id;
    
    @OneToOne(optional = false)
    @PrimaryKeyJoinColumn
    private User user;
    
    private Integer experienceYears;
    
    @Column(unique = true)
    private String licenseNumber;
    
    @Column(columnDefinition = "TEXT")
    private String specializations; // Comma-separated: PUNCTURE,WASH,BRAKE,BATTERY,AC,ENGINE
    
    private Double latitude;
    private Double longitude;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MechanicStatus currentStatus = MechanicStatus.OFFLINE; // AVAILABLE, ON_JOB, OFFLINE
    
    @Builder.Default
    private Double averageRating = 0.0;
    @Builder.Default
    private Integer totalCompleted = 0;
    @Builder.Default
    private Double totalEarnings = 0.0;
    
    private String bankAccount;
    
    @Builder.Default
    private Boolean isVerified = false;
    
    @Builder.Default
    private Integer ongoingJobsCount = 0;
}
