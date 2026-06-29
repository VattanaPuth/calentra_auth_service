package com.tech.sv.calentra.auth_service.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<?> HandleApiException(ApiException e){
        ErrorResponseException errorResponseException = new ErrorResponseException(e.getStatus(), e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(errorResponseException);
    }
}
