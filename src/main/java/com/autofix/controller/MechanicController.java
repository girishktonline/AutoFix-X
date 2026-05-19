package com.autofix.controller;

import com.autofix.entity.MechanicStatus;
import com.autofix.service.MechanicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mechanics")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowCredentials = "true")
public class MechanicController {
    private final MechanicService mechanicService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            Long mechanicId = 1L; // TODO: Extract from JWT
            return ResponseEntity.ok(mechanicService.getMechanicProfile(mechanicId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/status")
    public ResponseEntity<?> updateStatus(@RequestParam MechanicStatus status) {
        try {
            Long mechanicId = 1L; // TODO: Extract from JWT
            mechanicService.updateStatus(mechanicId, status);
            return ResponseEntity.ok("Status updated to: " + status);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/location")
    public ResponseEntity<?> updateLocation(
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        try {
            Long mechanicId = 1L; // TODO: Extract from JWT
            mechanicService.updateLocation(mechanicId, latitude, longitude);
            return ResponseEntity.ok("Location updated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableMechanics() {
        try {
            return ResponseEntity.ok(mechanicService.getAvailableMechanics());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
