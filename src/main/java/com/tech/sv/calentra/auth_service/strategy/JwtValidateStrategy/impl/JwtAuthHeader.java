package com.tech.sv.calentra.auth_service.strategy.JwtValidateStrategy.impl;

import org.springframework.stereotype.Service;

import com.tech.sv.calentra.auth_service.strategy.JwtValidateStrategy.TokenExtractor;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtAuthHeader implements TokenExtractor<HttpServletRequest, String>{

    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public String extract(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return null;
        }

        String token = authHeader.substring(BEARER_PREFIX.length()).trim();

        return token.isEmpty() ? null : token;
    }
}
