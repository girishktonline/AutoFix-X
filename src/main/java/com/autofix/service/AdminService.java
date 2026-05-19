package com.autofix.service;

import com.autofix.entity.BookingStatus;
import com.autofix.entity.PaymentStatus;
import com.autofix.repository.MechanicRepository;
import com.autofix.repository.ServiceBookingRepository;
import com.autofix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminService {
    private final ServiceBookingRepository bookingRepository;
    private final MechanicRepository mechanicRepository;
    private final UserRepository userRepository;

    // Get total bookings count
    public long getTotalBookings() {
        return bookingRepository.count();
    }

    // Get completed bookings count
    public long getCompletedBookings() {
        return bookingRepository.countByStatus(BookingStatus.COMPLETED);
    }

    // Get pending bookings count
    public long getPendingBookings() {
        return bookingRepository.countByStatus(BookingStatus.PENDING);
    }

    // Get total revenue from completed bookings
    public double getTotalRevenue() {
        return bookingRepository.findByStatus(BookingStatus.COMPLETED).stream()
                .mapToDouble(b -> b.getActualCost() != null ? b.getActualCost() * 0.30 : 0) // 30% platform commission
                .sum();
    }

    // Get platform commission from completed bookings
    public double getPlatformCommission() {
        return getTotalRevenue(); // Already calculated at 30%
    }

    // Get total mechanic earnings
    public double getTotalMechanicEarnings() {
        return bookingRepository.findByStatus(BookingStatus.COMPLETED).stream()
                .mapToDouble(b -> b.getActualCost() != null ? b.getActualCost() * 0.70 : 0) // 70% to mechanic
                .sum();
    }

    // Get active mechanics count
    public long getActiveMechanics() {
        return mechanicRepository.count();
    }

    // Get success rate (completed / total)
    public double getSuccessRate() {
        long total = getTotalBookings();
        if (total == 0) return 0;
        long completed = getCompletedBookings();
        return (double) completed / total * 100;
    }

    // Get average job value
    public double getAverageJobValue() {
        long completed = getCompletedBookings();
        if (completed == 0) return 0;
        double total = bookingRepository.findByStatus(BookingStatus.COMPLETED).stream()
                .mapToDouble(b -> b.getActualCost() != null ? b.getActualCost() : 0)
                .sum();
        return total / completed;
    }

    // Get top earning mechanics (by total completed jobs * rating)
    public String getTopMechanic() {
        var mechanics = mechanicRepository.findTopRatedAvailableMechanics();
        if (mechanics.isEmpty()) return "No mechanics";
        var topMechanic = mechanics.get(0);
        return topMechanic.getUser().getName() + " (Rating: " + topMechanic.getAverageRating() + ")";
    }

    // Get booking status breakdown
    public String getBookingBreakdown() {
        long pending = bookingRepository.countByStatus(BookingStatus.PENDING);
        long assigned = bookingRepository.countByStatus(BookingStatus.ASSIGNED);
        long inProgress = bookingRepository.countByStatus(BookingStatus.IN_PROGRESS);
        long completed = bookingRepository.countByStatus(BookingStatus.COMPLETED);
        long cancelled = bookingRepository.countByStatus(BookingStatus.CANCELLED);
        
        return String.format("Pending: %d, Assigned: %d, In Progress: %d, Completed: %d, Cancelled: %d",
                pending, assigned, inProgress, completed, cancelled);
    }
}
