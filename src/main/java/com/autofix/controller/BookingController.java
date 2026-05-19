package com.autofix.controller;

import com.autofix.dto.BookingRequestDTO;
import com.autofix.dto.BookingResponseDTO;
import com.autofix.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowCredentials = "true")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDTO request) {
        try {
            // Get customer ID from JWT token (would normally extract from security context)
            Long customerId = 1L; // TODO: Extract from JWT
            BookingResponseDTO booking = bookingService.createBooking(customerId, request);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating booking: " + e.getMessage());
        }
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<?> getMyBookings() {
        try {
            Long customerId = 1L; // TODO: Extract from JWT
            List<BookingResponseDTO> bookings = bookingService.getCustomerBookings(customerId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching bookings: " + e.getMessage());
        }
    }

    @GetMapping("/my-jobs")
    public ResponseEntity<?> getMyJobs() {
        try {
            Long mechanicId = 1L; // TODO: Extract from JWT
            List<BookingResponseDTO> jobs = bookingService.getMechanicJobs(mechanicId);
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching jobs: " + e.getMessage());
        }
    }

    @PostMapping("/{bookingId}/accept")
    public ResponseEntity<?> acceptJob(@PathVariable Long bookingId) {
        try {
            Long mechanicId = 1L; // TODO: Extract from JWT
            bookingService.acceptJob(bookingId, mechanicId);
            return ResponseEntity.ok("Job accepted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error accepting job: " + e.getMessage());
        }
    }

    @PostMapping("/{bookingId}/complete")
    public ResponseEntity<?> completeJob(@PathVariable Long bookingId, @RequestParam Double actualCost) {
        try {
            Long mechanicId = 1L; // TODO: Extract from JWT
            bookingService.completeJob(bookingId, mechanicId, actualCost);
            return ResponseEntity.ok("Job completed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error completing job: " + e.getMessage());
        }
    }

    @PostMapping("/{bookingId}/rate")
    public ResponseEntity<?> rateBooking(
            @PathVariable Long bookingId,
            @RequestParam Integer rating,
            @RequestParam String review,
            @RequestParam Boolean isCustomer) {
        try {
            Long userId = 1L; // TODO: Extract from JWT
            bookingService.rateBooking(bookingId, userId, rating, review, isCustomer);
            return ResponseEntity.ok("Rating submitted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error submitting rating: " + e.getMessage());
        }
    }

    @DeleteMapping("/{bookingId}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {
        try {
            Long customerId = 1L; // TODO: Extract from JWT
            bookingService.cancelBooking(bookingId, customerId);
            return ResponseEntity.ok("Booking cancelled");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error cancelling booking: " + e.getMessage());
        }
    }
}
