package com.shuttlemanagementsystem.ShuttleManagementSystem.dto;

import java.time.LocalDateTime;

public class BookingResponseDTO {
    private Long bookingId;
    private String startStopName;
    private String endStopName;
    private String shuttleVehicleNumber;
    private Double fare;
    private LocalDateTime bookingTime;
    private String status;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getStartStopName() {
        return startStopName;
    }

    public void setStartStopName(String startStopName) {
        this.startStopName = startStopName;
    }

    public String getEndStopName() {
        return endStopName;
    }

    public void setEndStopName(String endStopName) {
        this.endStopName = endStopName;
    }

    public String getShuttleVehicleNumber() {
        return shuttleVehicleNumber;
    }

    public void setShuttleVehicleNumber(String shuttleVehicleNumber) {
        this.shuttleVehicleNumber = shuttleVehicleNumber;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
