package com.tech.sv.calentra.auth_service.services;

import com.tech.sv.calentra.auth_service.entities.RefreshToken;
import org.springframework.http.ResponseEntity;

public interface LogoutService {
    RefreshToken logout(String refreshToken);
}
