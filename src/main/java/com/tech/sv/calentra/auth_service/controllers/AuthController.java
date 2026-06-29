package com.tech.sv.calentra.auth_service.controllers;

import com.tech.sv.calentra.auth_service.dtos.requests.RefreshTokenRequestDTO;
import com.tech.sv.calentra.auth_service.dtos.requests.RegisterRequestDTO;
import com.tech.sv.calentra.auth_service.entities.RefreshToken;
import com.tech.sv.calentra.auth_service.entities.Register;
import com.tech.sv.calentra.auth_service.mapper.RefreshTokenMapper;
import com.tech.sv.calentra.auth_service.mapper.RegisterMapper;
import com.tech.sv.calentra.auth_service.services.LogoutService;
import com.tech.sv.calentra.auth_service.services.RefreshTokenService;
import com.tech.sv.calentra.auth_service.services.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        RefreshToken refreshToken = refreshTokenMapper.toRefreshToken(request);
        refreshToken = refreshTokenServiceImpl.refreshToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(refreshTokenMapper.tofreRefreshTokenResponseDto(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody RefreshTokenRequestDTO request) {
        RefreshToken deleteRefreshToken = refreshTokenMapper.toRefreshToken(request);
        deleteRefreshToken = logoutServiceImpl.logout(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(refreshTokenMapper.tofreRefreshTokenResponseDto(deleteRefreshToken));
    }
}
