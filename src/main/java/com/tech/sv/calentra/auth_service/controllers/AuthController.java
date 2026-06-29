package com.tech.sv.calentra.auth_service.controllers;

import com.tech.sv.calentra.auth_service.dtos.requests.RegisterRequestDTO;
import com.tech.sv.calentra.auth_service.entities.Register;
import com.tech.sv.calentra.auth_service.mappers.RefreshTokenMapper;
import com.tech.sv.calentra.auth_service.mappers.RegisterMapper;
import com.tech.sv.calentra.auth_service.services.LogoutService;
import com.tech.sv.calentra.auth_service.services.RefreshTokenService;
import com.tech.sv.calentra.auth_service.services.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenMapper refreshTokenMapper;
    private final RefreshTokenService refreshTokenServiceImpl;
    private final RegisterMapper registerMapper;
    private final RegisterService registerServiceImpl;
    private final LogoutService logoutServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequestDTO){
        Register register = registerMapper.toRegister(registerRequestDTO);
        register = registerServiceImpl.register(register);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerMapper.toRegisterResponseDto(register));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh token missing");
        }
        Map<String, String> newRefreshToken = refreshTokenServiceImpl.refreshToken(refreshToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(refreshTokenMapper.tofreRefreshTokenResponseDto(newRefreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutUser(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            logoutServiceImpl.logout(refreshToken);
        }

        ResponseCookie clearAccessCookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie clearRefreshCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/auth")
                .maxAge(0)
                .build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, clearAccessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, clearRefreshCookie.toString())
                .build();
    }
}
