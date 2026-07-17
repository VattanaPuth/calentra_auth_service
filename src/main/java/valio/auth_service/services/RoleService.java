package valio.auth_service.services;

import java.util.List;
import java.util.UUID;

import valio.auth_service.dtos.requests.RoleRequestDTO;
import valio.auth_service.entities.Role;

public interface RoleService {
	Role getById(UUID roleId);
	Role create(RoleRequestDTO request);
	Role update(UUID roleId, RoleRequestDTO request);
	void delete(UUID roleId);
	List<Role> listAll();
    Role attachPermission(UUID roleId, UUID permissionId);
    Role detachPermission(UUID roleId, UUID permissionId);
}
