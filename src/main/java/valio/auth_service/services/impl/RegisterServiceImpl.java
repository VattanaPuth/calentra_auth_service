package valio.auth_service.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import valio.auth_service.entities.Register;
import valio.auth_service.repositories.RegisterRepository;
import valio.auth_service.services.RegisterService;

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
