package com.tech.sv.calentra.auth_service.exceptions;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{
    public ApiException(String message) {
        super(message);
    }
}

