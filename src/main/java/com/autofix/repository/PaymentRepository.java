package com.autofix.repository;

import com.autofix.entity.Payment;
import com.autofix.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByServiceBookingId(Long bookingId);
    
    long countByStatus(PaymentStatus status);
}
