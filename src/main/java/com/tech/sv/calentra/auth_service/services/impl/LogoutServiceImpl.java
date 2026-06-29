package com.tech.sv.calentra.auth_service.services.impl;

import com.tech.sv.calentra.auth_service.entities.RefreshToken;
import com.tech.sv.calentra.auth_service.exceptions.InvalidRefreshTokenException;
import com.tech.sv.calentra.auth_service.exceptions.ResourceNotFoundException;
import com.tech.sv.calentra.auth_service.repositories.RefreshTokenRepository;
import com.tech.sv.calentra.auth_service.services.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Map<String, String> logout(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new ResourceNotFoundException("Invalid Refresh Token");
        }

        RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token."));

        refreshTokenRepository.delete(token);

        return Map.of("message", "Logout successful");
    }
}
