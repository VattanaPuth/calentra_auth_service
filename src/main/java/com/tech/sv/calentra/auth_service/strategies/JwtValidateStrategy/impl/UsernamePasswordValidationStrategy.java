package com.tech.sv.calentra.auth_service.strategies.JwtValidateStrategy.impl;
import com.tech.sv.calentra.auth_service.exceptions.InvalidUsernamePasswordException;
import org.springframework.stereotype.Service;

import com.tech.sv.calentra.auth_service.dtos.requests.LoginRequestDTO;
import com.tech.sv.calentra.auth_service.strategies.JwtValidateStrategy.ValidationRuleStrategy;

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
