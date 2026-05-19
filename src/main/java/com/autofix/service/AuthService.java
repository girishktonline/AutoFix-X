package com.autofix.service;

import com.autofix.dto.AuthResponseDTO;
import com.autofix.dto.LoginDTO;
import com.autofix.dto.RegisterDTO;
import com.autofix.entity.*;
import com.autofix.repository.CustomerRepository;
import com.autofix.repository.MechanicRepository;
import com.autofix.repository.UserRepository;
import com.autofix.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final MechanicRepository mechanicRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public AuthResponseDTO register(RegisterDTO registerDTO) {
        // Check if email already exists
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            return AuthResponseDTO.builder()
                    .message("Email already registered")
                    .build();
        }
        
        // Create user
        User user = User.builder()
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .phone(registerDTO.getPhone())
                .name(registerDTO.getName())
                .role(registerDTO.getRole())
                .isActive(true)
                .build();
        
        User savedUser = userRepository.save(user);
        
        // Create role-specific profiles
        if (registerDTO.getRole() == UserRole.CUSTOMER) {
            Customer customer = Customer.builder()
                    .id(savedUser.getId())
                    .user(savedUser)
                    .address(registerDTO.getAddress())
                    .city(registerDTO.getCity())
                    .latitude(registerDTO.getLatitude())
                    .longitude(registerDTO.getLongitude())
                    .totalSpent(0.0)
                    .totalBookings(0)
                    .build();
            customerRepository.save(customer);
        } else if (registerDTO.getRole() == UserRole.MECHANIC) {
            Mechanic mechanic = Mechanic.builder()
                    .id(savedUser.getId())
                    .user(savedUser)
                    .experienceYears(registerDTO.getExperienceYears())
                    .licenseNumber(registerDTO.getLicenseNumber())
                    .specializations(registerDTO.getSpecializations())
                    .latitude(registerDTO.getLatitude())
                    .longitude(registerDTO.getLongitude())
                    .currentStatus(MechanicStatus.OFFLINE)
                    .isVerified(false)
                    .totalCompleted(0)
                    .totalEarnings(0.0)
                    .build();
            mechanicRepository.save(mechanic);
        }
        
        // Generate token
        String token = jwtTokenProvider.generateToken(savedUser.getId(), savedUser.getEmail());
        
        return AuthResponseDTO.builder()
                .token(token)
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .role(savedUser.getRole())
                .message("User registered successfully")
                .build();
    }
    
    public AuthResponseDTO login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElse(null);
        
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return AuthResponseDTO.builder()
                    .message("Invalid email or password")
                    .build();
        }
        
        if (!user.getIsActive()) {
            return AuthResponseDTO.builder()
                    .message("User account is deactivated")
                    .build();
        }
        
        // Generate token
        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
        
        return AuthResponseDTO.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .message("Login successful")
                .build();
    }
}
