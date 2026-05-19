package com.autofix.repository;

import com.autofix.entity.ServiceBooking;
import com.autofix.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceBookingRepository extends JpaRepository<ServiceBooking, Long> {
    List<ServiceBooking> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<ServiceBooking> findByMechanicIdOrderByCreatedAtDesc(Long mechanicId);
    List<ServiceBooking> findByStatus(BookingStatus status);
    List<ServiceBooking> findByStatusOrderByCreatedAtDesc(BookingStatus status);
    long countByStatus(BookingStatus status);
}
