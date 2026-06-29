package com.tech.sv.calentra.auth_service.dtos.responses;

import lombok.Data;

@Data
public class RegisterResponseDTO {
    private String email;
    private String username;
}
