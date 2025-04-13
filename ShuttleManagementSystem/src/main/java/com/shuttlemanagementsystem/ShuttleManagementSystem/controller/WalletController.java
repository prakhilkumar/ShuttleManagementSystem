package com.shuttlemanagementsystem.ShuttleManagementSystem.controller;

import com.shuttlemanagementsystem.ShuttleManagementSystem.dto.UserWalletResponseDTO;
import com.shuttlemanagementsystem.ShuttleManagementSystem.model.User;
import com.shuttlemanagementsystem.ShuttleManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class WalletController {

    private final UserRepository userRepository;

    @Autowired
    public WalletController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{userId}/wallet")
    public ResponseEntity<UserWalletResponseDTO> getWalletBalance(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->new RuntimeException("User not found"));
        UserWalletResponseDTO dto =new UserWalletResponseDTO();
        dto.setUserId(user.getId());
        dto.setBalance(user.getWalletBalance());
        return ResponseEntity.ok(dto);
    }
}
