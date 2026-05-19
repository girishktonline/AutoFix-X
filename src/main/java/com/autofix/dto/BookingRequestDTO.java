package com.autofix.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDTO {
    private Long vehicleId;
    private String serviceTypes;
    private String symptoms;
    private Double customerLatitude;
    private Double customerLongitude;
}
