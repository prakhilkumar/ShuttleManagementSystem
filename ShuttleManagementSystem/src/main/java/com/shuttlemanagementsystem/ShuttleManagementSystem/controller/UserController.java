package com.shuttlemanagementsystem.ShuttleManagementSystem.controller;

import com.shuttlemanagementsystem.ShuttleManagementSystem.dto.*;
import com.shuttlemanagementsystem.ShuttleManagementSystem.model.User;
import com.shuttlemanagementsystem.ShuttleManagementSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService =userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            User user =userService.registerUser(registrationDTO);

            user.setPassword(null);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDto) {

            TokenResponse tokenResponse = userService.loginUser(loginDto);
            return new ResponseEntity<>(tokenResponse, HttpStatus.OK);

    }


    @PostMapping("/recharge")
    public ResponseEntity<UserWalletResponseDTO> rechargeWallet(@RequestBody WalletRechargeDTO rechargeDTO) {
        UserWalletResponseDTO response = userService.rechargeWallet(rechargeDTO);
        return ResponseEntity.ok(response);
    }

}
