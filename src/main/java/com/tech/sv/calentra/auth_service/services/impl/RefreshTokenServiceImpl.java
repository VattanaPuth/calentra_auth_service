package com.tech.sv.calentra.auth_service.services.impl;

import com.tech.sv.calentra.auth_service.entities.RefreshToken;
import com.tech.sv.calentra.auth_service.exceptions.InvalidRefreshTokenException;
import com.tech.sv.calentra.auth_service.repositories.RefreshTokenRepository;
import com.tech.sv.calentra.auth_service.repositories.RegisterRepository;
import com.tech.sv.calentra.auth_service.services.RefreshTokenService;
import com.tech.sv.calentra.auth_service.utils.SignKey;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final RegisterRepository registerRepository;

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
        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String newJwt = Jwts.builder()
                .subject(getSubject)
                .issuedAt(new Date())
                .claim("authorities", authorities)
                .expiration(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
                .issuer("Calentra")
                .signWith(SignKey.getSecretKey())
                .compact();

        return token;
    }
}
