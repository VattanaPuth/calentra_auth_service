package com.tech.sv.calentra.auth_service.exceptions;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@RequiredArgsConstructor
public class ErrorResponseException {
    private final HttpStatus status;
    private final String msg;
}
