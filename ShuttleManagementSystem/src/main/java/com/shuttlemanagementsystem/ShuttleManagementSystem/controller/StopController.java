package com.shuttlemanagementsystem.ShuttleManagementSystem.controller;

import com.shuttlemanagementsystem.ShuttleManagementSystem.dto.RouteCreationDTO;
import com.shuttlemanagementsystem.ShuttleManagementSystem.dto.StopCreationDTO;
import com.shuttlemanagementsystem.ShuttleManagementSystem.model.Route;
import com.shuttlemanagementsystem.ShuttleManagementSystem.model.Stop;
import com.shuttlemanagementsystem.ShuttleManagementSystem.service.RouteService;
import com.shuttlemanagementsystem.ShuttleManagementSystem.service.StopService;
import com.shuttlemanagementsystem.ShuttleManagementSystem.service.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stop")
public class StopController {
    private final StopService stopService;

    @Autowired
    public StopController(StopService stopService) {
        this.stopService=stopService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createStop(@RequestBody StopCreationDTO stopCreationDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = userDetails.getUser().getRole();

            if (!"ADMIN".equalsIgnoreCase(role)) {
                throw new RuntimeException("Only admins can perform this action");
            }
            Stop stop = stopService.createStop(stopCreationDTO);
            return ResponseEntity.ok(stop);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
