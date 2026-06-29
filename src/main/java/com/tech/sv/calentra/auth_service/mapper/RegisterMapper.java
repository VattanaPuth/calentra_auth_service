package com.tech.sv.calentra.auth_service.mapper;

import com.tech.sv.calentra.auth_service.dtos.requests.RegisterRequestDTO;
import com.tech.sv.calentra.auth_service.dtos.responses.RegisterResponseDTO;
import com.tech.sv.calentra.auth_service.entities.Register;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
    Register toRegister(RegisterRequestDTO registerRequestDTO);
    RegisterResponseDTO toRegisterResponseDto(Register register);
}
