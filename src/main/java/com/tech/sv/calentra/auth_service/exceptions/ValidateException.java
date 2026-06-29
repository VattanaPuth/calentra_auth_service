package com.tech.sv.calentra.auth_service.exceptions;

import org.springframework.http.HttpStatus;

public class ValidateException extends ApiException{
    public ValidateException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
