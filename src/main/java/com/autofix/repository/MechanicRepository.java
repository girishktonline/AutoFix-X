package com.autofix.repository;

import com.autofix.entity.Mechanic;
import com.autofix.entity.MechanicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
    Optional<Mechanic> findByUserId(Long userId);
    List<Mechanic> findByCurrentStatus(MechanicStatus status);
    
    @Query("SELECT m FROM Mechanic m WHERE m.currentStatus = 'AVAILABLE' " +
           "AND m.isVerified = true " +
           "AND m.specializations LIKE CONCAT('%', :serviceType, '%') " +
           "ORDER BY m.averageRating DESC")
    List<Mechanic> findAvailableMechanicsForService(@Param("serviceType") String serviceType);
    
    @Query("SELECT m FROM Mechanic m WHERE m.currentStatus = 'AVAILABLE' " +
           "AND m.isVerified = true " +
           "ORDER BY m.averageRating DESC")
    List<Mechanic> findTopRatedAvailableMechanics();
}
