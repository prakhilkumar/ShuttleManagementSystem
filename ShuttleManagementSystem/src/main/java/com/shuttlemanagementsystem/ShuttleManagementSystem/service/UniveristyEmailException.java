package com.shuttlemanagementsystem.ShuttleManagementSystem.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UniveristyEmailException extends RuntimeException {
    public UniveristyEmailException(String message) {
        super(message);
    }
}
