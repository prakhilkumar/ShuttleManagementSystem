package com.shuttlemanagementsystem.ShuttleManagementSystem.service;

import com.shuttlemanagementsystem.ShuttleManagementSystem.dto.RouteCreationDTO;
import com.shuttlemanagementsystem.ShuttleManagementSystem.model.Route;

public interface RouteService {
    Route createRoute(RouteCreationDTO routeCreationDTO);
}
