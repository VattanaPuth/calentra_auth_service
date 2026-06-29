package com.tech.sv.calentra.auth_service.services;

import com.tech.sv.calentra.auth_service.entities.RefreshToken;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(UUID userId);
    boolean isTokenExpired(RefreshToken token);
    RefreshToken refreshToken(String requestToken);
}
