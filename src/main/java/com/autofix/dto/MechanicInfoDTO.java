package com.autofix.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MechanicInfoDTO {
    private Long mechanicId;
    private String name;
    private Double rating;
    private Double latitude;
    private Double longitude;
    private String phone;
}
