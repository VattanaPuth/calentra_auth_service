package valio.auth_service.services.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import valio.auth_service.entities.RefreshToken;
import valio.auth_service.exceptions.InvalidRefreshTokenException;
import valio.auth_service.exceptions.ResourceNotFoundException;
import valio.auth_service.repositories.RefreshTokenRepository;
import valio.auth_service.services.LogoutService;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Map<String, String> logout(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new ResourceNotFoundException();
        }

        RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(InvalidRefreshTokenException::new);

        refreshTokenRepository.delete(token);

        return Map.of("message", "Logout successful");
    }
}
