package com.tech.sv.calentra.auth_service.strategy.HttpRequestValidateRule.impl;

import com.tech.sv.calentra.auth_service.dtos.requests.LoginRequestDTO;
import com.tech.sv.calentra.auth_service.exceptions.ValidateException;
import com.tech.sv.calentra.auth_service.strategy.HttpRequestValidateRule.HttpRequestValidateRule;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class JwtValidateUsernamePassword implements HttpRequestValidateRule<LoginRequestDTO> {
    @Override
    public void validate(LoginRequestDTO loginRequest) {
        if(loginRequest.getEmail() == null ||
                loginRequest.getEmail().isEmpty() ||
                loginRequest.getPassword() == null ||
                loginRequest.getPassword().isEmpty()) {
            throw new ValidateException("Username and password are required.");
        }
    }
}
