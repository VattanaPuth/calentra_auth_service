package valio.auth_service.mappers;

import org.mapstruct.Mapper;

import valio.auth_service.dtos.requests.RegisterRequestDTO;
import valio.auth_service.dtos.responses.RegisterResponseDTO;
import valio.auth_service.entities.Register;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
    Register toRegister(RegisterRequestDTO registerRequestDTO);
    RegisterResponseDTO toRegisterResponseDto(Register register);
}
