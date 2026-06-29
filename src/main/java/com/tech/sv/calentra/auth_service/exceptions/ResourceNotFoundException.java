package com.tech.sv.calentra.auth_service.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException{
    public ResourceNotFoundException(String msg) {
        super(HttpStatus.NOT_FOUND , "");
    }

    public ResourceNotFoundException(HttpStatus status, String msg) {
        super(status, msg);
    }
}
