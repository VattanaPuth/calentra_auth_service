package com.tech.sv.calentra.auth_service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tech.sv.calentra.auth_service.entities.Permission;

public interface PermissionRepository extends JpaRepository<Permission, UUID>{
	Optional<Permission> findByNameIgnoreCase(String name);
}
