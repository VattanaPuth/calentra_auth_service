package valio.auth_service.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import valio.auth_service.dtos.requests.PermissionRequestDTO;
import valio.auth_service.entities.Permission;
import valio.auth_service.exceptions.DuplicatePermissionException;
import valio.auth_service.exceptions.ResourceNotFoundException;
import valio.auth_service.repositories.PermissionRepository;
import valio.auth_service.services.PermissionService;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

	private final PermissionRepository permissionRepository;

	@Transactional
	@Override
	public Permission create(PermissionRequestDTO request) {
		if (permissionRepository.existsByName(request.name())) {
			throw new DuplicatePermissionException();
		}
		Permission permission = new Permission();
		permission.setName(request.name().toUpperCase());
		permission.setDescription(request.description());
		return permissionRepository.save(permission);
	}

	@Transactional
	@Override
	public Permission update(UUID id, PermissionRequestDTO request) {
		Permission permission = permissionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());

		permission.setName(request.name());
		permission.setDescription(request.description());
		return permissionRepository.save(permission);
	}

	@Transactional
	@Override
	public void delete(UUID id) {
		if (!permissionRepository.existsById(id)) {
			throw new DuplicatePermissionException();
		}
		if (permissionRepository.isAttachedToAnyRole(id)) {
			throw new ResourceNotFoundException();
		}
		permissionRepository.deleteById(id);
	}

	@Override
	public Permission getById(UUID id) {
		return permissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
	}
	
	@Override
	public List<Permission> listAll() {
		return permissionRepository.findAll().stream().toList();
	}

}
