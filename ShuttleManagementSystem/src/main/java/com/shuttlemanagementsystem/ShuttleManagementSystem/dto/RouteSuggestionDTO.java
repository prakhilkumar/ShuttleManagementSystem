package com.shuttlemanagementsystem.ShuttleManagementSystem.dto;

import java.util.List;

public class RouteSuggestionDTO {
    private Long routeId;
    private String routeName;
    private List<String> stops;
    private double cost;
    private double estimatedTravelTime;

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public List<String> getStops() {
        return stops;
    }

    public void setStops(List<String> stops) {
        this.stops = stops;
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
