package com.autofix.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDTO {
    private Long bookingId;
    private String status;
    private String paymentStatus;
    private Double estimatedCost;
    private Double actualCost;
    private Integer etaMinutes;
    private Double distanceKm;
    private String serviceTypes;
    private String symptoms;
    private Integer customerRating;
    private Integer mechanicRating;
    private MechanicInfoDTO mechanic;
    private LocalDateTime bookingTime;
}
