package com.autofix.dto;

import com.autofix.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDTO {
    private String email;
    private String password;
    private String phone;
    private String name;
    private UserRole role;
    private String address;
    private String city;
    private Double latitude;
    private Double longitude;
    private Integer experienceYears;
    private String licenseNumber;
    private String specializations;
}
