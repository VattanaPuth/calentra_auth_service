package valio.auth_service.services;

import java.util.List;
import java.util.UUID;

import valio.auth_service.dtos.requests.PermissionRequestDTO;
import valio.auth_service.entities.Permission;

public interface PermissionService {
	Permission getById(UUID id);
	Permission create(PermissionRequestDTO request);
	Permission update(UUID id, PermissionRequestDTO request);
	void delete(UUID id);
	List<Permission> listAll();
}
