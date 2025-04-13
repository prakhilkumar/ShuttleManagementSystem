package com.shuttlemanagementsystem.ShuttleManagementSystem.service;

import com.shuttlemanagementsystem.ShuttleManagementSystem.dto.*;
import com.shuttlemanagementsystem.ShuttleManagementSystem.model.User;

public interface UserService {
    User registerUser(UserRegistrationDTO userRegistrationDTO);
    TokenResponse loginUser(LoginDTO loginDto);
    UserWalletResponseDTO rechargeWallet(WalletRechargeDTO rechargeDTO);
}
