package com.tech.sv.calentra.auth_service.strategy.JwtValidateStrategy.impl;

import com.tech.sv.calentra.auth_service.exceptions.ValidateException;
import com.tech.sv.calentra.auth_service.strategy.JwtValidateStrategy.ValidationRule;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class JwtValidateContentLength implements ValidationRule<HttpServletRequest> {

    @Override
    public void validate(HttpServletRequest request) {
        if (request.getContentLength() <= 0) {
            throw new ValidateException("Request body is required");
        }
    }
}
