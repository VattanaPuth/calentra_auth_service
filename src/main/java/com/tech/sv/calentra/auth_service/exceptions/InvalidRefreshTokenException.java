package com.tech.sv.calentra.auth_service.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends ApiException{

    public InvalidRefreshTokenException(String msg) {
        super(HttpStatus.FORBIDDEN, msg);
    }
}
