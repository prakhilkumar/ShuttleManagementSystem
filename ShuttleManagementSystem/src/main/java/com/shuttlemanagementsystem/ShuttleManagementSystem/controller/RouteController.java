package com.shuttlemanagementsystem.ShuttleManagementSystem.controller;

import com.shuttlemanagementsystem.ShuttleManagementSystem.dto.RouteCreationDTO;
import com.shuttlemanagementsystem.ShuttleManagementSystem.model.Route;
import com.shuttlemanagementsystem.ShuttleManagementSystem.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/routes")
public class RouteController {

    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping
    public ResponseEntity<Route> createRoute(@RequestBody RouteCreationDTO routeCreationDTO) {
        Route route = routeService.createRoute(routeCreationDTO);
        return ResponseEntity.ok(route);
    }
}
