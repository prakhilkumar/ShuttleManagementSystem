package com.shuttlemanagementsystem.ShuttleManagementSystem.dto;

public class ShuttleOptionDTO {
    private Long shuttleId;
    private String shuttleVehicleNumber;
    private double cost;
    private double estimatedTravelTime;

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
}
