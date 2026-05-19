package com.autofix.service;

import com.autofix.entity.Mechanic;
import com.autofix.entity.MechanicStatus;
import com.autofix.repository.MechanicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MechanicService {
    private final MechanicRepository mechanicRepository;

    // Update mechanic status
    public void updateStatus(Long mechanicId, MechanicStatus status) {
        Mechanic mechanic = mechanicRepository.findByUserId(mechanicId)
                .orElseThrow(() -> new RuntimeException("Mechanic not found"));
        
        mechanic.setCurrentStatus(status);
        mechanicRepository.save(mechanic);
        log.info("Mechanic {} status updated to: {}", mechanic.getUser().getName(), status);
    }

    // Update mechanic real-time location
    public void updateLocation(Long mechanicId, double latitude, double longitude) {
        Mechanic mechanic = mechanicRepository.findByUserId(mechanicId)
                .orElseThrow(() -> new RuntimeException("Mechanic not found"));
        
        mechanic.setLatitude(latitude);
        mechanic.setLongitude(longitude);
        mechanicRepository.save(mechanic);
        log.debug("Mechanic {} location updated: {},{}", mechanicId, latitude, longitude);
        
        // TODO: Broadcast via WebSocket to customer if job is in progress
    }

    // Get mechanic profile
    public Mechanic getMechanicProfile(Long mechanicId) {
        return mechanicRepository.findByUserId(mechanicId)
                .orElseThrow(() -> new RuntimeException("Mechanic not found"));
    }

    // Get available mechanics
    public List<Mechanic> getAvailableMechanics() {
        return mechanicRepository.findByCurrentStatus(MechanicStatus.AVAILABLE);
    }

    // Verify mechanic (admin only)
    public void verifyMechanic(Long mechanicId) {
        Mechanic mechanic = mechanicRepository.findById(mechanicId)
                .orElseThrow(() -> new RuntimeException("Mechanic not found"));
        
        mechanic.setIsVerified(true);
        mechanicRepository.save(mechanic);
        log.info("Mechanic {} verified", mechanic.getUser().getName());
    }

    // Update specializations
    public void updateSpecializations(Long mechanicId, String specializations) {
        Mechanic mechanic = mechanicRepository.findByUserId(mechanicId)
                .orElseThrow(() -> new RuntimeException("Mechanic not found"));
        
        mechanic.setSpecializations(specializations);
        mechanicRepository.save(mechanic);
        log.info("Mechanic {} specializations updated to: {}", mechanic.getUser().getName(), specializations);
    }
}
