package com.tech.sv.calentra.auth_service.filters;

import com.tech.sv.calentra.auth_service.strategy.HttpRequestValidateRule.DoFilterInternalValidateRule;
import com.tech.sv.calentra.auth_service.utils.SignKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtVerifyFilter extends OncePerRequestFilter {

    private final DoFilterInternalValidateRule doFilterInternalValidateRule;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.equals("/auth/register")
                || path.equals("/auth/login")
                || path.equals("/auth/refresh");
    }

    @Override
    protected void doFilterInternal(
                @NonNull HttpServletRequest request,
                @NonNull HttpServletResponse response,
                @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = getAccessTokenFromCookie(request);

        if (accessToken == null) {
            accessToken = doFilterInternalValidateRule.authHeaderValidate(request);
        }
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Jws<Claims> claimJws = Jwts.parser()
                .verifyWith(SignKey.getSecretKey())
                .build()
                .parseSignedClaims(accessToken);

        Claims body = claimJws.getPayload();
        String username = body.getSubject();

        List<String> authorities = body.get("authorities", List.class);
        Set<SimpleGrantedAuthority> grantedAuthorities = authorities == null
                ? Set.of()
                : authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        Authentication getAuthentication = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(getAuthentication);
        filterChain.doFilter(request, response);
    }

    private String getAccessTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("access_token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
