package com.tech.sv.calentra.auth_service.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.sv.calentra.auth_service.dtos.requests.LoginRequestDTO;
import com.tech.sv.calentra.auth_service.entities.RefreshToken;
import com.tech.sv.calentra.auth_service.entities.Register;
import com.tech.sv.calentra.auth_service.exceptions.ResourceNotFoundException;
import com.tech.sv.calentra.auth_service.repositories.RegisterRepository;
import com.tech.sv.calentra.auth_service.services.RefreshTokenService;
import com.tech.sv.calentra.auth_service.strategy.HttpRequestValidateRule.impl.JwtValidateContentLength;
import com.tech.sv.calentra.auth_service.strategy.HttpRequestValidateRule.impl.JwtValidateUsernamePassword;
import com.tech.sv.calentra.auth_service.strategy.HttpRequestValidateRule.impl.JwtMaxAttempt;
import com.tech.sv.calentra.auth_service.utils.SignKey;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtValidateContentLength jwtValidateContentLength;
    private final JwtValidateUsernamePassword jwtValidateUsernamePassword;
    private final JwtMaxAttempt jwtMaxAttempt;
    private final RegisterRepository registerRepository;
    private final RefreshTokenService refreshTokenServiceImpl;
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objMapper = new ObjectMapper();
        jwtValidateContentLength.validate(request);
        try {
            LoginRequestDTO loginRequest = objMapper.readValue(request.getInputStream(), LoginRequestDTO.class);
            jwtValidateUsernamePassword.validate(loginRequest);
            request.setAttribute("LOGIN_EMAIL", loginRequest.getEmail());
            Register register = registerRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Email Not Found"));
            jwtMaxAttempt.validate(register);
            Authentication auth = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            return authenticationManager.authenticate(auth);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        String email = authResult.getName();
        Register register = registerRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email Not Found"));
        jwtMaxAttempt.resetFailedAttempts(register);
        List<String> authorities = authResult.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        String accessToken = Jwts.builder()
                           .subject(authResult.getName())
                           .issuedAt(new Date())
                           .claim("authorities", authorities)
                           .expiration(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
                           .issuer("Calentra")
                           .signWith(SignKey.getSecretKey())
                           .compact();

        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(false) // true in production HTTPS
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        RefreshToken refreshToken = refreshTokenServiceImpl.createRefreshToken(register.getId());

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/auth")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = """
            {
                "message": "Login successful",
                "tokenType": "Bearer"
            }
            """;
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String email = (String) request.getAttribute("LOGIN_EMAIL");

        if (failed instanceof LockedException) {
            response.setStatus(423); // Locked

            response.getWriter().write("""
            {
                "status": "LOCKED",
                "message": "%s"
            }
            """.formatted(failed.getMessage()));

            response.getWriter().flush();
            return;
        }

        if (email != null) {
            registerRepository.findByEmail(email).ifPresent(jwtMaxAttempt::increaseFailedAttempts);
            Register updatedRegister = registerRepository.findByEmail(email).orElse(null);

            if (updatedRegister != null && Boolean.FALSE.equals(updatedRegister.getIsAccountNonLocked())) {
                response.setStatus(423);

                response.getWriter().write("""
                {
                    "status": "LOCKED",
                    "message": "Too many failed login attempts. Account locked for 15 minutes"
                }
                """);

                response.getWriter().flush();
                return;
            }
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("""
            {
                "status": "UNAUTHORIZED",
                "message": "Invalid email or password"
            }
            """);
        response.getWriter().flush();
    }
}
