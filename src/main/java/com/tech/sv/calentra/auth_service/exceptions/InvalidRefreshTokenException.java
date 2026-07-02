package com.tech.sv.calentra.auth_service.exceptions;

import lombok.Data;

@Data
public class InvalidRefreshTokenException extends RuntimeException{
    private String message;
}
