package com.tech.sv.calentra.auth_service.services.impl;

import com.tech.sv.calentra.auth_service.entities.Register;
import com.tech.sv.calentra.auth_service.repositories.RegisterRepository;
import com.tech.sv.calentra.auth_service.services.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final RegisterRepository registerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Register register(Register userRegister) {
        userRegister.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        return registerRepository.save(userRegister);
    }
}
