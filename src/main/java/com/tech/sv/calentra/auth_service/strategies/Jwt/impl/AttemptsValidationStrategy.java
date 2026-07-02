package com.tech.sv.calentra.auth_service.strategies.Jwt.impl;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

import com.tech.sv.calentra.auth_service.entities.Register;
import com.tech.sv.calentra.auth_service.exceptions.ResourceNotFoundException;
import com.tech.sv.calentra.auth_service.repositories.RegisterRepository;
import com.tech.sv.calentra.auth_service.strategies.Jwt.ValidationRuleStrategy;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttemptsValidationStrategy implements ValidationRuleStrategy<Register> {

    private final RegisterRepository registerRepository;

    private final int maxLoginAttempts = 5;
    private final int lockMinutes = 15;

    @Override
    public void validate(Register register) {
        if (register == null) {
            throw new ResourceNotFoundException();
        }

        if (!Boolean.FALSE.equals(register.getIsAccountNonLocked())) {
            return;
        }

        LocalDateTime lockUntil = register.getLockUntil();

        if (lockUntil == null) {
            unlock(register);
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "dd MMM yyyy hh:mm a",
                Locale.ENGLISH
        );

        if (lockUntil.isAfter(LocalDateTime.now())) {
            throw new LockedException("Account is locked until " + lockUntil.format(formatter));
        }

        unlock(register);
    }

    public void increaseFailedAttempts(Register register) {
        if (register == null) {
            throw new ResourceNotFoundException();
        }

        int failedAttempts = register.getFailedLoginAttempts() == null
                ? 0
                : register.getFailedLoginAttempts();

        failedAttempts++;

        register.setFailedLoginAttempts(failedAttempts);

        if (failedAttempts >= maxLoginAttempts) {
            register.setIsAccountNonLocked(false);
            register.setLockUntil(LocalDateTime.now().plusMinutes(lockMinutes));
        }

        registerRepository.save(register);
    }

    public void resetFailedAttempts(Register register) {
        if (register == null) {
            throw new ResourceNotFoundException();
        }

        unlock(register);
    }

    private void unlock(Register register) {
        register.setFailedLoginAttempts(0);
        register.setIsAccountNonLocked(true);
        register.setLockUntil(null);

        registerRepository.save(register);
    }
}

