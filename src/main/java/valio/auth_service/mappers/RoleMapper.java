package valio.auth_service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import valio.auth_service.dtos.requests.RoleRequestDTO;
import valio.auth_service.dtos.responses.RoleResponseDTO;
import valio.auth_service.entities.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
	
	@Mapping(target="id", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	Role toRole(RoleRequestDTO toRoleResponseDTO);
	RoleResponseDTO toResponseDTO (Role role);
}
