package com.shuttlemanagementsystem.ShuttleManagementSystem.service;

import com.shuttlemanagementsystem.ShuttleManagementSystem.dto.RouteCreationDTO;
import com.shuttlemanagementsystem.ShuttleManagementSystem.model.*;
import com.shuttlemanagementsystem.ShuttleManagementSystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;
    private final StopRepository stopRepository;
    private final UserRepository userRepository;

    @Autowired
    public RouteServiceImpl(RouteRepository routeRepository,
                            RouteStopRepository routeStopRepository,
                            StopRepository stopRepository,
                            UserRepository userRepository) {
        this.routeRepository = routeRepository;
        this.routeStopRepository = routeStopRepository;
        this.stopRepository = stopRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Route createRoute(RouteCreationDTO dto) {
        User admin = userRepository.findById(dto.getAdminId())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Route route = new Route();
        route.setName(dto.getName());
        route.setDescription(dto.getDescription());
        route.setAdmin(admin);
        route.setCreatedAt(LocalDateTime.now());
        route = routeRepository.save(route);

        List<RouteStop> routeStops = new ArrayList<>();
        int seq = 1;
        for (Long stopId : dto.getStopIds()) {
            Stop stop = stopRepository.findById(stopId)
                    .orElseThrow(() -> new RuntimeException("Stop not found: " + stopId));
            RouteStop rs = new RouteStop();
            rs.setRoute(route);
            rs.setStop(stop);
            rs.setSequence(seq++);
            routeStops.add(rs);
        }
        routeStopRepository.saveAll(routeStops);
        return route;
    }
}
