package com.autofix.repository;

import com.autofix.entity.MechanicEarnings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MechanicEarningsRepository extends JpaRepository<MechanicEarnings, Long> {
    List<MechanicEarnings> findByMechanicIdOrderByCreatedAtDesc(Long mechanicId);
    
    Optional<MechanicEarnings> findByBookingId(Long bookingId);
    
    @Query("SELECT SUM(me.mechanicEarnings) FROM MechanicEarnings me " +
           "WHERE me.mechanic.id = :mechanicId AND me.createdAt >= :startDate AND me.createdAt <= :endDate")
    Double getTodayEarnings(@Param("mechanicId") Long mechanicId, 
                           @Param("startDate") LocalDateTime startDate,
                           @Param("endDate") LocalDateTime endDate);
}
