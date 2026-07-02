package com.tech.sv.calentra.auth_service.strategies.Jwt.impl;
import com.tech.sv.calentra.auth_service.exceptions.InvalidUsernamePasswordException;
import com.tech.sv.calentra.auth_service.strategies.Jwt.ValidationRuleStrategy;

import org.springframework.stereotype.Service;

import com.tech.sv.calentra.auth_service.dtos.requests.LoginRequestDTO;

@Service
public class UsernamePasswordValidationStrategy implements ValidationRuleStrategy<LoginRequestDTO> {
    @Override
    public void validate(LoginRequestDTO loginRequest) {
        if(loginRequest.getEmail() == null ||
                loginRequest.getEmail().isEmpty() ||
                loginRequest.getPassword() == null ||
                loginRequest.getPassword().isEmpty()) {
            throw new InvalidUsernamePasswordException();
        }
    }
}
