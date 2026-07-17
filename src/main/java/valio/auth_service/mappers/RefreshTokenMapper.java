package valio.auth_service.mappers;

import org.mapstruct.Mapper;

import valio.auth_service.dtos.requests.RefreshTokenRequestDTO;
import valio.auth_service.dtos.responses.RefreshTokenResponseDTO;
import valio.auth_service.entities.RefreshToken;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {
    RefreshToken toRefreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO);
    RefreshTokenResponseDTO tofreRefreshTokenResponseDto(RefreshToken refreshToken);
}
