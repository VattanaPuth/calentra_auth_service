package com.tech.sv.calentra.auth_service.services;

import java.util.List;
import java.util.UUID;

import com.tech.sv.calentra.auth_service.dtos.requests.PermissionRequestDTO;
import com.tech.sv.calentra.auth_service.entities.Permission;

public interface PermissionService {
	Permission getById(UUID id);
	Permission create(PermissionRequestDTO request);
	Permission update(UUID id, PermissionRequestDTO request);
	void delete(UUID id);
	List<Permission> listAll();
}
