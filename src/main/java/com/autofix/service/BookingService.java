package com.autofix.service;

import com.autofix.dto.BookingRequestDTO;
import com.autofix.dto.BookingResponseDTO;
import com.autofix.dto.MechanicInfoDTO;
import com.autofix.entity.*;
import com.autofix.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingService {
    private final ServiceBookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final MechanicRepository mechanicRepository;
    private final VehicleRepository vehicleRepository;
    private final LocationService locationService;
    private final PaymentService paymentService;
    private final NotificationService notificationService;

    public BookingResponseDTO createBooking(Long customerId, BookingRequestDTO request) {
        log.info("Creating booking for customer: {}", customerId);

        Customer customer = customerRepository.findByUserId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Vehicle vehicle = vehicleRepository.findByIdAndCustomerId(request.getVehicleId(), customer.getId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Mechanic assignedMechanic = findBestMechanicForService(
                request.getServiceTypes(),
                request.getCustomerLatitude(),
                request.getCustomerLongitude()
        );

        if (assignedMechanic == null) {
            throw new RuntimeException("No available mechanics for this service");
        }

        double distanceKm = locationService.calculateDistance(
                request.getCustomerLatitude(),
                request.getCustomerLongitude(),
                assignedMechanic.getLatitude(),
                assignedMechanic.getLongitude()
        );
        
        int etaMinutes = locationService.calculateETA(distanceKm);

        ServiceBooking booking = ServiceBooking.builder()
                .customer(customer)
                .vehicle(vehicle)
                .mechanic(assignedMechanic)
                .serviceTypes(request.getServiceTypes())
                .symptoms(request.getSymptoms())
                .status(BookingStatus.ASSIGNED)
                .paymentStatus(PaymentStatus.PENDING)
                .customerLatitude(request.getCustomerLatitude())
                .customerLongitude(request.getCustomerLongitude())
                .mechanicLatitude(assignedMechanic.getLatitude())
                .mechanicLongitude(assignedMechanic.getLongitude())
                .distanceKm(distanceKm)
                .etaMinutes(etaMinutes)
                .estimatedCost(calculateEstimatedCost(request.getServiceTypes()))
                .build();

        booking = bookingRepository.save(booking);
        notificationService.notifyMechanicNewJob(assignedMechanic, booking);
        notificationService.notifyCustomerBookingConfirmed(customer, booking);

        return convertToResponseDTO(booking);
    }

    private Mechanic findBestMechanicForService(String serviceTypes, double customerLat, double customerLng) {
        List<Mechanic> availableMechanics = mechanicRepository.findByCurrentStatus(MechanicStatus.AVAILABLE);

        return availableMechanics.stream()
                .filter(m -> matchesServiceType(m, serviceTypes))
                .min((m1, m2) -> {
                    int ratingCompare = Double.compare(m2.getAverageRating(), m1.getAverageRating());
                    if (ratingCompare != 0) return ratingCompare;
                    
                    double dist1 = locationService.calculateDistance(customerLat, customerLng, m1.getLatitude(), m1.getLongitude());
                    double dist2 = locationService.calculateDistance(customerLat, customerLng, m2.getLatitude(), m2.getLongitude());
                    return Double.compare(dist1, dist2);
                })
                .orElse(null);
    }

    private boolean matchesServiceType(Mechanic mechanic, String serviceTypes) {
        String[] requiredServices = serviceTypes.split(",");
        String[] mechanicServices = mechanic.getSpecializations().split(",");
        
        for (String required : requiredServices) {
            for (String offered : mechanicServices) {
                if (required.trim().equals(offered.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    private double calculateEstimatedCost(String serviceTypes) {
        String[] services = serviceTypes.split(",");
        return 500 + (services.length - 1) * 300;
    }

    public List<BookingResponseDTO> getCustomerBookings(Long customerId) {
        Customer customer = customerRepository.findByUserId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        return bookingRepository.findByCustomerIdOrderByCreatedAtDesc(customer.getId())
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<BookingResponseDTO> getMechanicJobs(Long mechanicId) {
        Mechanic mechanic = mechanicRepository.findByUserId(mechanicId)
                .orElseThrow(() -> new RuntimeException("Mechanic not found"));
        
        return bookingRepository.findByMechanicIdOrderByCreatedAtDesc(mechanic.getId())
                .stream()
                .filter(b -> b.getStatus() != BookingStatus.COMPLETED && b.getStatus() != BookingStatus.CANCELLED)
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void acceptJob(Long bookingId, Long mechanicId) {
        ServiceBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Mechanic mechanic = mechanicRepository.findByUserId(mechanicId)
                .orElseThrow(() -> new RuntimeException("Mechanic not found"));

        if (!booking.getMechanic().getId().equals(mechanic.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        booking.setStatus(BookingStatus.IN_PROGRESS);
        booking.setArrivalTime(LocalDateTime.now());
        bookingRepository.save(booking);

        mechanic.setCurrentStatus(MechanicStatus.ON_JOB);
        mechanicRepository.save(mechanic);

        notificationService.notifyCustomerMechanicAccepted(booking.getCustomer(), booking);
    }

    @Transactional
    public void completeJob(Long bookingId, Long mechanicId, Double actualCost) {
        ServiceBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Mechanic mechanic = mechanicRepository.findByUserId(mechanicId)
                .orElseThrow(() -> new RuntimeException("Mechanic not found"));

        if (!booking.getMechanic().getId().equals(mechanic.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        booking.setCompletionTime(LocalDateTime.now());
        booking.setActualCost(actualCost);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        bookingRepository.save(booking);

        paymentService.createPaymentForBooking(booking);

        mechanic.setCurrentStatus(MechanicStatus.AVAILABLE);
        mechanic.setTotalCompleted(mechanic.getTotalCompleted() + 1);
        mechanic.setTotalEarnings(mechanic.getTotalEarnings() + (actualCost * 0.7));
        mechanicRepository.save(mechanic);

        Customer customer = booking.getCustomer();
        customer.setTotalSpent(customer.getTotalSpent() + actualCost);
        customer.setTotalBookings(customer.getTotalBookings() + 1);
        customerRepository.save(customer);

        notificationService.notifyCustomerJobCompleted(booking.getCustomer(), booking);
    }

    @Transactional
    public void rateBooking(Long bookingId, Long userId, Integer rating, String review, Boolean isCustomer) {
        ServiceBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (isCustomer) {
            booking.setMechanicRating(rating);
            booking.setMechanicReview(review);
            
            Mechanic mechanic = booking.getMechanic();
            double newAvgRating = (mechanic.getAverageRating() * mechanic.getTotalCompleted() + rating) 
                    / (mechanic.getTotalCompleted() + 1);
            mechanic.setAverageRating(newAvgRating);
            mechanicRepository.save(mechanic);
        } else {
            booking.setCustomerRating(rating);
            booking.setCustomerReview(review);
        }

        bookingRepository.save(booking);
    }

    @Transactional
    public void cancelBooking(Long bookingId, Long customerId) {
        ServiceBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Unauthorized");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel completed bookings");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        if (booking.getMechanic() != null) {
            Mechanic mechanic = booking.getMechanic();
            mechanic.setCurrentStatus(MechanicStatus.AVAILABLE);
            mechanicRepository.save(mechanic);
        }
    }

    private BookingResponseDTO convertToResponseDTO(ServiceBooking booking) {
        MechanicInfoDTO mechanicInfo = null;
        if (booking.getMechanic() != null) {
            mechanicInfo = MechanicInfoDTO.builder()
                    .mechanicId(booking.getMechanic().getId())
                    .name(booking.getMechanic().getUser().getName())
                    .rating(booking.getMechanic().getAverageRating())
                    .latitude(booking.getMechanic().getLatitude())
                    .longitude(booking.getMechanic().getLongitude())
                    .phone(booking.getMechanic().getUser().getPhone())
                    .build();
        }

        return BookingResponseDTO.builder()
                .bookingId(booking.getId())
                .status(booking.getStatus().toString())
                .paymentStatus(booking.getPaymentStatus().toString())
                .estimatedCost(booking.getEstimatedCost())
                .actualCost(booking.getActualCost())
                .etaMinutes(booking.getEtaMinutes())
                .distanceKm(booking.getDistanceKm())
                .serviceTypes(booking.getServiceTypes())
                .symptoms(booking.getSymptoms())
                .customerRating(booking.getCustomerRating())
                .mechanicRating(booking.getMechanicRating())
                .mechanic(mechanicInfo)
                .bookingTime(booking.getBookingTime())
                .build();
    }
}

