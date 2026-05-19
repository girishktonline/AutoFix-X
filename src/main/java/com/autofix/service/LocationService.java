package com.autofix.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LocationService {
    
    // Haversine formula to calculate distance between two GPS coordinates
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in kilometers
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Distance in km
        
        log.debug("Calculated distance: {} km between ({},{}) and ({},{})", 
                distance, lat1, lon1, lat2, lon2);
        
        return distance;
    }
    
    // Calculate ETA based on distance (assuming 20 km/h average in city)
    public int calculateETA(double distanceKm) {
        final double AVERAGE_SPEED_KMH = 20;
        int etaMinutes = (int) Math.ceil((distanceKm / AVERAGE_SPEED_KMH) * 60) + 5; // +5 min buffer
        log.debug("Calculated ETA: {} minutes for {} km", etaMinutes, distanceKm);
        return etaMinutes;
    }
    
    // Update location for real-time tracking
    public void updateMechanicLocation(Long mechanicId, double latitude, double longitude) {
        log.debug("Mechanic {} location updated to: {},{}", mechanicId, latitude, longitude);
        // Will be persisted via MechanicService
    }
}
