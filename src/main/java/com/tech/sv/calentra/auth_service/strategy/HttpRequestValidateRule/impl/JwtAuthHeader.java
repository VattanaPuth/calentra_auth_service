package com.tech.sv.calentra.auth_service.strategy.HttpRequestValidateRule.impl;

import com.tech.sv.calentra.auth_service.strategy.HttpRequestValidateRule.DoFilterInternalValidateRule;
import com.tech.sv.calentra.auth_service.utils.SignKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
public class JwtAuthHeader implements DoFilterInternalValidateRule {

    @Override
    public String authHeaderValidate(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7).trim();

        if (token.isEmpty()) {
            return null;
        }

        return token;
    }
}
