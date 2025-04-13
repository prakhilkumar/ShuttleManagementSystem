package com.shuttlemanagementsystem.ShuttleManagementSystem.service;

import com.shuttlemanagementsystem.ShuttleManagementSystem.dto.*;
import com.shuttlemanagementsystem.ShuttleManagementSystem.model.*;
import com.shuttlemanagementsystem.ShuttleManagementSystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final StopRepository stopRepository;
    private final ShuttleRepository shuttleRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;


    @Autowired
    public BookingServiceImpl(StopRepository stopRepository,
                              ShuttleRepository shuttleRepository,
                              UserRepository userRepository,
                              BookingRepository bookingRepository,
                              RouteRepository routeRepository) {
        this.stopRepository = stopRepository;
        this.shuttleRepository = shuttleRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;

    }

    @Override
    public List<ShuttleOptionDTO> getAvailableShuttles(BookingRequestDTO bookingRequest) {
        // Validate that the user exists.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        if(user==null){
            throw new RuntimeException("user not exist");
        }

        // Retrieve all stops (for demo purposes; in production, use spatial queries)
        List<Stop> stops = stopRepository.findAll();
        if (stops.isEmpty()) {
            throw new RuntimeException("No shuttle stops available");
        }

        // Determine the nearest pickup and destination stops based on provided coordinates.
        Stop startStop = findNearestStop(bookingRequest.getCurrentLatitude(),
                bookingRequest.getCurrentLongitude(), stops);
        Stop endStop = findNearestStop(bookingRequest.getDestinationLatitude(),
                bookingRequest.getDestinationLongitude(), stops);

        // Calculate route distance (in km) using the Haversine formula.
        double routeDistance = calculateDistance(startStop.getLatitude(), startStop.getLongitude(),
                endStop.getLatitude(), endStop.getLongitude());

        // Define cost factors.
        double costPerKm = 3.0;          // Base cost per kilometer.
//        double costPerMinute = 0.5;      // Cost per minute of travel.
        double defaultSpeed = 30.0;      // Default shuttle speed (km/h) if avgSpeed not provided.
//        double waitingTimeFactor = 5.0;  // Additional waiting time (minutes per km of pickup distance).

        double baseCost = routeDistance * costPerKm;
        double bookingBaseCharge=20;
        // Retrieve all shuttles.
        List<Shuttle> shuttles = shuttleRepository.findAll();
        if (shuttles.isEmpty()) {
            throw new RuntimeException("No shuttle available");
        }

        // Filter shuttles within 1 km of the user's pickup location, compute cost and travel time,
        // sort by cost, and return only the top 4.
        List<ShuttleOptionDTO> options = shuttles.stream()
                .filter(shuttle -> {
                    if (shuttle.getCurrentLatitude() == null || shuttle.getCurrentLongitude() == null) {
                        return false;
                    }
                    if (shuttle.getCapacity() != null && shuttle.getCapacity() <= 0) {
                        return false;
                    }
                    double pickupDistance = calculateDistance(
                            bookingRequest.getCurrentLatitude(),
                            bookingRequest.getCurrentLongitude(),
                            shuttle.getCurrentLatitude(),
                            shuttle.getCurrentLongitude());
                    return pickupDistance <= 1.0;
                })
                .map(shuttle -> {
//                    double pickupDistance = calculateDistance(
//                            bookingRequest.getCurrentLatitude(),
//                            bookingRequest.getCurrentLongitude(),
//                            shuttle.getCurrentLatitude(),
//                            shuttle.getCurrentLongitude());
                    // Additional waiting time based on pickup distance.

//                    double additionalWaitTime = pickupDistance * waitingTimeFactor;
                    // Use shuttle's avgSpeed if available; otherwise, use defaultSpeed.

                    double shuttleSpeed = (shuttle.getAvgSpeed() != null ? shuttle.getAvgSpeed() : defaultSpeed);
                    // Base travel time along the route (in minutes).
                    double travelTime = (routeDistance / shuttleSpeed) * 60;
//                    double totalTravelTime = baseTravelTime + additionalWaitTime;
//                    double travelTimeCost = totalTravelTime * costPerMinute;
                    double finalCost = baseCost+bookingBaseCharge;
                    double roundedCost = BigDecimal.valueOf(finalCost).setScale(1, RoundingMode.HALF_UP).doubleValue();
                    double roundedTravelTime = BigDecimal.valueOf(travelTime).setScale(1, RoundingMode.HALF_UP).doubleValue();

                    ShuttleOptionDTO option = new ShuttleOptionDTO();
                    option.setShuttleId(shuttle.getId());
                    option.setShuttleVehicleNumber(shuttle.getVehicleNumber());
                    option.setCost(roundedCost);
                    option.setEstimatedTravelTime(roundedTravelTime);
                    return option;
                })
                .sorted(Comparator.comparingDouble(ShuttleOptionDTO::getCost))
                .limit(4)
                .collect(Collectors.toList());

        if (options.isEmpty()) {
            throw new RuntimeException("No nearby shuttle available");
        }
        return options;
    }

    @Override
    public ConfirmedBookingResponseDTO confirmBooking(ConfirmBookingRequestDTO bookingRequest) {
        // Validate that the user exists.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        if(user==null){
            throw new RuntimeException("user not exist");
        }

        // Validate that the selected shuttle exists.
        Shuttle shuttle = shuttleRepository.findById(bookingRequest.getSelectedShuttleId())
                .orElseThrow(() -> new RuntimeException("Shuttle not found"));

        if (shuttle.getCapacity() == null || shuttle.getCapacity() <= 0) {
            throw new RuntimeException("Shuttle is fully booked");
        }
        // Retrieve all stops.
        List<Stop> stops = stopRepository.findAll();
        if (stops.isEmpty()) {
            throw new RuntimeException("No shuttle stops available");
        }

        // Determine the nearest pickup and destination stops.
        Stop startStop = findNearestStop(bookingRequest.getCurrentLatitude(),
                bookingRequest.getCurrentLongitude(), stops);
        Stop endStop = findNearestStop(bookingRequest.getDestinationLatitude(),
                bookingRequest.getDestinationLongitude(), stops);

        // Calculate route distance using Haversine formula.
        double routeDistance = calculateDistance(startStop.getLatitude(), startStop.getLongitude(),
                endStop.getLatitude(), endStop.getLongitude());

        double costPerKm = 3.0;
        double bookingBaseCharge=20;
//        double costPerMinute = 0.5;
        double defaultSpeed = 30.0;
//        double waitingTimeFactor = 5.0;
        double baseCost = routeDistance * costPerKm;

        double pickupDistance = calculateDistance(
                bookingRequest.getCurrentLatitude(),
                bookingRequest.getCurrentLongitude(),
                shuttle.getCurrentLatitude(),
                shuttle.getCurrentLongitude());
//        double additionalWaitTime = pickupDistance * waitingTimeFactor;
        double shuttleSpeed = (shuttle.getAvgSpeed() != null ? shuttle.getAvgSpeed() : defaultSpeed);
        double travelTime = (routeDistance / shuttleSpeed) * 60;
//        double totalTravelTime = baseTravelTime + additionalWaitTime;
//        double travelTimeCost = totalTravelTime * costPerMinute;
        double finalCost = baseCost + bookingBaseCharge;
        double roundedCost = BigDecimal.valueOf(finalCost).setScale(1, RoundingMode.HALF_UP).doubleValue();
        double roundedTravelTime = BigDecimal.valueOf(travelTime).setScale(1, RoundingMode.HALF_UP).doubleValue();

        // If user's wallet balance is insufficient, return a failure response.
        if (user.getWalletBalance().compareTo(BigDecimal.valueOf(roundedCost)) < 0) {
            ConfirmedBookingResponseDTO response = new ConfirmedBookingResponseDTO();
            response.setUserId(user.getId());
            response.setStatus("FAILED: Insufficient wallet balance");
            return response;
        }

        // Deduct the cost from the user's wallet.
        user.setWalletBalance(user.getWalletBalance().subtract(BigDecimal.valueOf(roundedCost)));
        userRepository.save(user);

        // Decrease shuttle capacity
        int updatedCapacity = shuttle.getCapacity() - 1;
        shuttle.setCapacity(updatedCapacity);

// Mark as UNAVAILABLE if capacity becomes zero
        if (updatedCapacity <= 0) {
            shuttle.setCurrentStatus("UNAVAILABLE");
        } else {
            shuttle.setCurrentStatus("AVAILABLE");
        }

        shuttleRepository.save(shuttle);

        // Create and save the booking record.
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShuttle(shuttle);
        booking.setStartStop(startStop);
        booking.setEndStop(endStop);
        booking.setBookingTime(LocalDateTime.now());
        LocalDateTime estimatedEnd = LocalDateTime.now().plusMinutes((long) travelTime);
        booking.setEstimatedEndTime(estimatedEnd);
        booking.setTravelDate(LocalDate.now());
        booking.setFare(BigDecimal.valueOf(roundedCost));
        booking.setPointsDeducted(BigDecimal.valueOf(roundedCost));
        booking.setStatus("CONFIRMED");

        booking = bookingRepository.save(booking);

        // Prepare the response.
        ConfirmedBookingResponseDTO response = new ConfirmedBookingResponseDTO();
        response.setBookingId(booking.getId());
        response.setUserId(user.getId());
        response.setShuttleId(shuttle.getId());
        response.setShuttleVehicleNumber(shuttle.getVehicleNumber());
        response.setCost(roundedCost);
        response.setEstimatedTravelTime(roundedTravelTime);
        response.setBookingTime(booking.getBookingTime());
        response.setStatus(booking.getStatus());
        return response;
    }

    @Scheduled(fixedRate = 60000) // every 1 min
    public void autoCompleteRides() {
        LocalDateTime now = LocalDateTime.now();

        List<Booking> activeBookings = bookingRepository.findByStatus("CONFIRMED");

        for (Booking booking : activeBookings) {
            if (booking.getEstimatedEndTime().isBefore(now)) {
                Shuttle shuttle = booking.getShuttle();

                shuttle.setCapacity(shuttle.getCapacity() + 1);
                shuttle.setCurrentStatus("AVAILABLE");

                booking.setStatus("COMPLETED");

                shuttleRepository.save(shuttle);
                bookingRepository.save(booking);
            }
        }
    }


    public List<TripHistoryDTO> getTripHistory(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);

        return bookings.stream().map(booking -> {
            TripHistoryDTO dto = new TripHistoryDTO();
            dto.setBookingId(booking.getId());
            dto.setBookingTime(booking.getBookingTime());
            dto.setTravelDate(booking.getTravelDate());
            dto.setStartStopName(booking.getStartStop().getName());
            dto.setEndStopName(booking.getEndStop().getName());
            dto.setFare(booking.getFare());
            return dto;
        }).collect(Collectors.toList());
    }



    // Helper method: Calculate distance using the Haversine formula (returns distance in km).
    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int R = 6371; // Earth's radius in km.
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // Helper method: Find the nearest stop from a list of stops based on given latitude and longitude.
    private Stop findNearestStop(Double latitude, Double longitude, List<Stop> stops) {
        return stops.stream()
                .min((s1, s2) -> {
                    double d1 = calculateDistance(latitude, longitude, s1.getLatitude(), s1.getLongitude());
                    double d2 = calculateDistance(latitude, longitude, s2.getLatitude(), s2.getLongitude());
                    return Double.compare(d1, d2);
                })
                .orElseThrow(() -> new RuntimeException("No stops found"));
    }
}
