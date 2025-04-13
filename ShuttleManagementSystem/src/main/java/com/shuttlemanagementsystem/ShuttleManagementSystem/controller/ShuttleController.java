package com.shuttlemanagementsystem.ShuttleManagementSystem.controller;

import com.shuttlemanagementsystem.ShuttleManagementSystem.dto.ShuttleCreationDTO;
import com.shuttlemanagementsystem.ShuttleManagementSystem.model.Shuttle;
import com.shuttlemanagementsystem.ShuttleManagementSystem.service.ShuttleService;
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
@RequestMapping("/api/shuttle")
public class ShuttleController {
    public final ShuttleService shuttleService;
    @Autowired
    public ShuttleController(ShuttleService shuttleService){
        this.shuttleService=shuttleService;
    }
    @PostMapping("/create")
    public ResponseEntity<?> createShuttle(@RequestBody ShuttleCreationDTO shuttleCreationDTO) {
    try {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getUser().getRole();

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only admins can perform this action");
        }
        Shuttle shuttle = shuttleService.createShuttle(shuttleCreationDTO);
        return ResponseEntity.ok(shuttle);
    }
    catch (RuntimeException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    }

}
