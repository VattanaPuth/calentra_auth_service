package com.tech.sv.calentra.auth_service.dtos.requests;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String email;
    private String role;
    private String username;
    private String password;
}
