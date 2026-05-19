package com.autofix.controller;

import com.autofix.dto.LocationUpdateDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LocationWebSocketController {

    /**
     * Receive location updates from mechanics and broadcast to customers
     * Frontend sends to: /app/location/update
     * Frontend listens to: /topic/location/booking/{bookingId}
     */
    @MessageMapping("/location/update")
    @SendTo("/topic/location/broadcast")
    public LocationUpdateDTO broadcastLocation(LocationUpdateDTO locationUpdate) {
        return locationUpdate;
    }

    /**
     * Receive booking-specific location updates
     */
    @MessageMapping("/location/booking/{bookingId}")
    @SendTo("/topic/location/booking/{bookingId}")
    public LocationUpdateDTO broadcastBookingLocation(LocationUpdateDTO locationUpdate) {
        return locationUpdate;
    }
}
