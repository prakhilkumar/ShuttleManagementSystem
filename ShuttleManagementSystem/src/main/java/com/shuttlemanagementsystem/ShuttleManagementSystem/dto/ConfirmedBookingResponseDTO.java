package com.shuttlemanagementsystem.ShuttleManagementSystem.dto;

import java.time.LocalDateTime;

public class ConfirmedBookingResponseDTO {
    private Long bookingId;
    private Long userId;
    private Long shuttleId;
    private String shuttleVehicleNumber;
    private double cost;
    private double estimatedTravelTime;
    private LocalDateTime bookingTime;
    private String status;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShuttleId() {
        return shuttleId;
    }

    public void setShuttleId(Long shuttleId) {
        this.shuttleId = shuttleId;
    }

    public String getShuttleVehicleNumber() {
        return shuttleVehicleNumber;
    }

    public void setShuttleVehicleNumber(String shuttleVehicleNumber) {
        this.shuttleVehicleNumber = shuttleVehicleNumber;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getEstimatedTravelTime() {
        return estimatedTravelTime;
    }

    public void setEstimatedTravelTime(double estimatedTravelTime) {
        this.estimatedTravelTime = estimatedTravelTime;
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
