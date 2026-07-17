package com.tech.sv.calentra.auth_service.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tech.sv.calentra.auth_service.dtos.requests.PermissionRequestDTO;
import com.tech.sv.calentra.auth_service.dtos.responses.PermissionResponseDTO;
import com.tech.sv.calentra.auth_service.entities.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
	
	@Mapping(target = "id", ignore = true)
	Permission toPermission(PermissionRequestDTO permissionRequestDTO);
	PermissionResponseDTO toPermissionResponseDTO (Permission permission);
}
