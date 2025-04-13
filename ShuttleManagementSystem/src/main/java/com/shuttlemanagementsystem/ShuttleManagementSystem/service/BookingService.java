package com.shuttlemanagementsystem.ShuttleManagementSystem.service;

import com.shuttlemanagementsystem.ShuttleManagementSystem.dto.*;

import java.util.List;

public interface BookingService {
    List<ShuttleOptionDTO> getAvailableShuttles(BookingRequestDTO bookingRequest);
    ConfirmedBookingResponseDTO confirmBooking(ConfirmBookingRequestDTO bookingRequest);
    List<TripHistoryDTO> getTripHistory(Long userId);
}
