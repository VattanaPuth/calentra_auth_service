package valio.auth_service.services;

import java.util.UUID;

import valio.auth_service.entities.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(UUID userId);
    RefreshToken refreshToken(String requestToken);
    boolean isTokenExpired(RefreshToken token);
}
