package com.autofix.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationUpdateDTO {
    private Long bookingId;
    private Long mechanicId;
    private Double latitude;
    private Double longitude;
    private String mechanicName;
    private String mechanicPhone;
    private Integer etaMinutes;
    private String status; // ASSIGNED, EN_ROUTE, ON_SITE, COMPLETED
    private Long timestamp;
}
