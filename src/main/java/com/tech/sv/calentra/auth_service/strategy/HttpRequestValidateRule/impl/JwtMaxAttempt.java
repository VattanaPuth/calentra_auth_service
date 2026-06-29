package com.tech.sv.calentra.auth_service.strategy.HttpRequestValidateRule.impl;

import com.tech.sv.calentra.auth_service.entities.Register;
import com.tech.sv.calentra.auth_service.exceptions.ResourceNotFoundException;
import com.tech.sv.calentra.auth_service.repositories.RegisterRepository;
import com.tech.sv.calentra.auth_service.strategy.HttpRequestValidateRule.HttpRequestValidateRule;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtMaxAttempt implements HttpRequestValidateRule<Register> {

    private final RegisterRepository registerRepository;
    private final int maxLoginAttempts = 5;

    @Override
    public void validate(Register register) {

        if (Boolean.FALSE.equals(register.getIsAccountNonLocked())) {
            throw new LockedException("Account is locked");
        }

        int failedAttempts = register.getFailedLoginAttempts() == null
                ? 0
                : register.getFailedLoginAttempts();

        if (failedAttempts >= maxLoginAttempts) {
            register.setIsAccountNonLocked(false);
            registerRepository.save(register);

            throw new LockedException("Too many failed login attempts");
        }
    }

    @Transactional
    public void increaseFailedAttempts(Register register) {
        if (register == null) {
            throw new ResourceNotFoundException("Register is null");
        }

        int failedAttempts = register.getFailedLoginAttempts() == null
                ? 0
                : register.getFailedLoginAttempts();

        failedAttempts++;
        register.setFailedLoginAttempts(failedAttempts);

        if (failedAttempts >= maxLoginAttempts) {
            register.setIsAccountNonLocked(false);
        }

        registerRepository.save(register);
    }

    @Transactional
    public void resetFailedAttempts(Register register) {
        if (register == null) {
            throw new ResourceNotFoundException("Register is null");
        }

        register.setFailedLoginAttempts(0);
        register.setIsAccountNonLocked(true);

        registerRepository.save(register);
    }

}
