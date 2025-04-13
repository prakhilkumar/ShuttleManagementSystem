package com.shuttlemanagementsystem.ShuttleManagementSystem.controller;

import com.shuttlemanagementsystem.ShuttleManagementSystem.dto.*;
import com.shuttlemanagementsystem.ShuttleManagementSystem.service.BookingService;
import com.shuttlemanagementsystem.ShuttleManagementSystem.service.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService){

        this.bookingService = bookingService;
    }

    @PostMapping("/options")
    public ResponseEntity<List<ShuttleOptionDTO>> getAvailableShuttleOptions(@RequestBody BookingRequestDTO bookingRequest) {
        List<ShuttleOptionDTO> options = bookingService.getAvailableShuttles(bookingRequest);
        return ResponseEntity.ok(options);
    }

    @PostMapping("/confirm")
    public ResponseEntity<ConfirmedBookingResponseDTO> confirmBooking(@RequestBody ConfirmBookingRequestDTO request) {
        ConfirmedBookingResponseDTO response = bookingService.confirmBooking(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<TripHistoryDTO>> getTripHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId=userDetails.getUser().getId();
        List<TripHistoryDTO> response = bookingService.getTripHistory(userId);
        return ResponseEntity.ok(response);
    }

}
