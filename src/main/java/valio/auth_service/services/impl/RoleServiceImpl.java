package valio.auth_service.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import valio.auth_service.dtos.requests.RoleRequestDTO;
import valio.auth_service.entities.Permission;
import valio.auth_service.entities.Role;
import valio.auth_service.exceptions.DuplicateRoleException;
import valio.auth_service.exceptions.ResourceNotFoundException;
import valio.auth_service.repositories.PermissionRepository;
import valio.auth_service.repositories.RoleRepository;
import valio.auth_service.services.RoleService;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

	private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    
    @Override
    public Role getById(UUID roleId) {
        return roleRepository.findByIdWithPermissions(roleId).orElseThrow(() -> new ResourceNotFoundException());
    }
    
    @Transactional
    @Override
    public Role create(RoleRequestDTO request) {
        if (roleRepository.existsByName(request.name())) {
            throw new DuplicateRoleException();
        }
        Role role = new Role();
        role.setName(request.name().toUpperCase());
        role.setDescription(request.description());
        role.setPermissions(permissions(request.permissionIds()));
        return roleRepository.save(role);
    }

    @Transactional
    @Override
    public Role update(UUID roleId, RoleRequestDTO request) {
        Role role = getById(roleId);
        role.setName(request.name());
        role.setDescription(request.description());
        role.setPermissions(permissions(request.permissionIds()));
        return roleRepository.save(role);
    }

    @Transactional
    @Override
    public void delete(UUID roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException());
        roleRepository.delete(role);
    }

    @Override
    public List<Role> listAll() {
        return roleRepository.findAllWithPermissions().stream().toList();
    }

	@Transactional
	@Override
	public Role attachPermission(UUID roleId, UUID permissionId) {
		Role role = getById(roleId);
		Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new ResourceNotFoundException());
	    role.getPermissions().add(permission);
	    return roleRepository.save(role);
	}

	@Transactional
	@Override
	public Role detachPermission(UUID roleId, UUID permissionId) {
	    Role role = getById(roleId);
	    boolean removed = role.getPermissions().removeIf(p -> p.getId().equals(permissionId));
		if (!removed) {
			throw new ResourceNotFoundException();
		}
	    return roleRepository.save(role);
	}
	
    private Set<Permission> permissions(Set<UUID> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        Set<Permission> found = new HashSet<>(permissionRepository.findAllById(ids));
        if (found.size() != ids.size()) {
            throw new ResourceNotFoundException();
        }
        return found;
    }

}
