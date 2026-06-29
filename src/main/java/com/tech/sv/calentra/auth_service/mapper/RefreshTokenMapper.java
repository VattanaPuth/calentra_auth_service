package com.tech.sv.calentra.auth_service.mapper;

import com.tech.sv.calentra.auth_service.dtos.requests.RefreshTokenRequestDTO;
import com.tech.sv.calentra.auth_service.dtos.responses.RefreshTokenResponseDTO;
import com.tech.sv.calentra.auth_service.entities.RefreshToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {
    RefreshToken toRefreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO);
    RefreshTokenResponseDTO tofreRefreshTokenResponseDto(RefreshToken refreshToken);
}
