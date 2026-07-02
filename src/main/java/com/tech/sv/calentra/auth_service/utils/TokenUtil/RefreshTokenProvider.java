package com.tech.sv.calentra.auth_service.utils.TokenUtil;

import java.time.Duration;
import java.util.UUID;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.tech.sv.calentra.auth_service.entities.RefreshToken;
import com.tech.sv.calentra.auth_service.services.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RefreshTokenProvider {

    private final String ISSUER_PATH = "/auth";
    private final Duration REFRESH_TOKEN_TTL = Duration.ofDays(7);
    private  final RefreshTokenService refreshTokenServiceImpl;

    public RefreshToken refreshToken(UUID registerId) {
    	return refreshTokenServiceImpl.createRefreshToken(registerId);
    }
    
    public ResponseCookie generateRefreshCookie(RefreshToken refreshToken) {
        return ResponseCookie.from("refresh_token", refreshToken.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path(ISSUER_PATH)
                .maxAge(REFRESH_TOKEN_TTL)
                .build();
    }
}
