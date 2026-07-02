package com.tech.sv.calentra.auth_service.utils.TokenUtil;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.tech.sv.calentra.auth_service.utils.SignKey;

import io.jsonwebtoken.Jwts;

@Component
public class AccessTokenProvider {

	private final String ISSUER_PATH = "/";
    private final String ISSUER = "Calentra";
    private final String AUTHORITIES = "authorities";

    public String generateAccessToken(String subject, Collection<? extends GrantedAuthority> authorities) {
        
    	List<String> authorityClaim = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .claim(AUTHORITIES, authorityClaim)
                .expiration(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
                .issuer(ISSUER)
                .signWith(SignKey.getSecretKey())
                .compact();
    }
    
    public ResponseCookie generateAccessCookie(String accessToken) {
        return ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path(ISSUER_PATH)
                .maxAge(Duration.ofMinutes(15))
                .build();
    }
}
