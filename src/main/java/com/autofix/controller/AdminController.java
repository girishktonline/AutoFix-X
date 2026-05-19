package com.autofix.controller;

import com.autofix.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowCredentials = "true")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        try {
            Map<String, Object> dashboard = new HashMap<>();
            dashboard.put("totalBookings", adminService.getTotalBookings());
            dashboard.put("completedBookings", adminService.getCompletedBookings());
            dashboard.put("pendingBookings", adminService.getPendingBookings());
            dashboard.put("totalRevenue", adminService.getTotalRevenue());
            dashboard.put("platformCommission", adminService.getPlatformCommission());
            dashboard.put("mechanicEarnings", adminService.getTotalMechanicEarnings());
            dashboard.put("activeMechanics", adminService.getActiveMechanics());
            dashboard.put("successRate", adminService.getSuccessRate());
            dashboard.put("averageJobValue", adminService.getAverageJobValue());
            dashboard.put("topMechanic", adminService.getTopMechanic());
            dashboard.put("bookingBreakdown", adminService.getBookingBreakdown());
            
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/analytics")
    public ResponseEntity<?> getAnalytics() {
        try {
            Map<String, Object> analytics = new HashMap<>();
            analytics.put("successRate", adminService.getSuccessRate());
            analytics.put("averageJobValue", adminService.getAverageJobValue());
            analytics.put("totalEarnings", adminService.getTotalMechanicEarnings() + adminService.getPlatformCommission());
            analytics.put("platformShare", adminService.getPlatformCommission());
            
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
