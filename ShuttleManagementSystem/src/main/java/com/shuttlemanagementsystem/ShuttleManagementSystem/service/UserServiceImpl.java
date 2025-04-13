package com.shuttlemanagementsystem.ShuttleManagementSystem.service;

import com.shuttlemanagementsystem.ShuttleManagementSystem.dto.*;
import com.shuttlemanagementsystem.ShuttleManagementSystem.model.User;
import com.shuttlemanagementsystem.ShuttleManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserTokenService userTokenService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,UserTokenService userTokenService) {
        this.userRepository = userRepository;

        this.userTokenService=userTokenService;
    }

    @Override
    public User registerUser(UserRegistrationDTO dto) {
        if (!dto.getEmail().toLowerCase().endsWith("@gla.ac.in")) {
            throw new UniveristyEmailException("University email id needed. Please use a valid @gla.ac.in email address.");
        }
        if (dto.getRole().toLowerCase().equals("admin")) {
            throw new RuntimeException("Admins cannot be registered");
        }

        userRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistsException("User with email " + dto.getEmail() + " already exists");
        });

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole() != null ? dto.getRole() : "STUDENT");
        user.setWalletBalance(new BigDecimal("100.0"));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    public TokenResponse loginUser(LoginDTO loginDto) {
        System.out.print(loginDto.getEmail()+"  *********************&&&&&&&&&&"+loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return userTokenService.createToken(loginDto.getEmail());
    }

    @Override
    public UserWalletResponseDTO rechargeWallet(WalletRechargeDTO rechargeDTO) {
        User admin = userRepository.findById(rechargeDTO.getAdminId())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!"ADMIN".equalsIgnoreCase(admin.getRole())) {
            throw new RuntimeException("Unauthorized: Only admins can recharge wallets");
        }

        User student = userRepository.findById(rechargeDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setWalletBalance(student.getWalletBalance().add(
                BigDecimal.valueOf(rechargeDTO.getAmount())
        ));
        userRepository.save(student);

        UserWalletResponseDTO response = new UserWalletResponseDTO();
        response.setUserId(student.getId());
        response.setBalance(student.getWalletBalance());
        return response;
    }
}
