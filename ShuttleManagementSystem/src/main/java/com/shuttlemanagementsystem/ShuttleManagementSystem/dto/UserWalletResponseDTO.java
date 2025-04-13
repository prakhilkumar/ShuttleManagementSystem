package com.shuttlemanagementsystem.ShuttleManagementSystem.dto;

import java.math.BigDecimal;

public class UserWalletResponseDTO {
    private Long userId;
    private BigDecimal balance;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
