package com.tech.sv.calentra.auth_service.dtos.requests;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String username;
    private String password;
}

