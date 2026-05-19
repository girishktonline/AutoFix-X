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
public class AuthResponseDTO {
    private String token;
    private Long userId;
    private String email;
    private String name;
    private UserRole role;
    private String message;
}
