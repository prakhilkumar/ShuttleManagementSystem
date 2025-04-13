package com.shuttlemanagementsystem.ShuttleManagementSystem.dto;

public class ConfirmBookingRequestDTO {

    private Double currentLatitude;
    private Double currentLongitude;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private Long selectedShuttleId;

    public Double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(Double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public Double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(Double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public Double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(Double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public Double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(Double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public Long getSelectedShuttleId() {
        return selectedShuttleId;
    }

    public void setSelectedShuttleId(Long selectedShuttleId) {
        this.selectedShuttleId = selectedShuttleId;
    }
}
