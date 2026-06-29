package com.tech.sv.calentra.auth_service.services;

import com.tech.sv.calentra.auth_service.entities.RefreshToken;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface LogoutService {
    Map<String, String> logout(String refreshToken);
}
