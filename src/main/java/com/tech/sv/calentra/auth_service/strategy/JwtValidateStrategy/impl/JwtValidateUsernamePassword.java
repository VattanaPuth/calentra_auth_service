package com.tech.sv.calentra.auth_service.strategy.JwtValidateStrategy.impl;
import org.springframework.stereotype.Service;

import com.tech.sv.calentra.auth_service.dtos.requests.LoginRequestDTO;
import com.tech.sv.calentra.auth_service.exceptions.ValidateException;
import com.tech.sv.calentra.auth_service.strategy.JwtValidateStrategy.ValidationRule;

@Service
public class JwtValidateUsernamePassword implements ValidationRule<LoginRequestDTO> {
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
