package com.tech.sv.calentra.auth_service.services.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tech.sv.calentra.auth_service.entities.RefreshToken;
import com.tech.sv.calentra.auth_service.exceptions.InvalidRefreshTokenException;
import com.tech.sv.calentra.auth_service.repositories.RefreshTokenRepository;
import com.tech.sv.calentra.auth_service.repositories.RegisterRepository;
import com.tech.sv.calentra.auth_service.services.RefreshTokenService;
import com.tech.sv.calentra.auth_service.utils.AccessTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final RegisterRepository registerRepository;
    private final AccessTokenProvider accessTokenProvider;

    @Override
    public RefreshToken createRefreshToken(UUID userId) {
        RefreshToken token = new RefreshToken();
        token.setRegister(registerRepository.getReferenceById(userId));
        token.setExpiryDate(LocalDateTime.now().plusDays(7));
        token.setRefreshToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(token);
    }

    @Override
    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(LocalDateTime.now());
    }

    @Override
    public RefreshToken refreshToken(String requestToken) {

        RefreshToken token = refreshTokenRepository.findByRefreshToken(requestToken)
                .orElseThrow(InvalidRefreshTokenException::new);

        if (isTokenExpired(token)) {
            refreshTokenRepository.delete(token);
            throw new InvalidRefreshTokenException();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String getSubject = authentication.getName();

        accessTokenProvider.generateAccessToken(getSubject, authentication.getAuthorities());

        return token;
    }
}
