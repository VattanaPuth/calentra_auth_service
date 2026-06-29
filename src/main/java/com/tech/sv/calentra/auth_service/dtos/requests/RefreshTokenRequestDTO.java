package com.tech.sv.calentra.auth_service.dtos.requests;

import lombok.Data;

@Data
public class RefreshTokenRequestDTO {
    private String refreshToken;
}