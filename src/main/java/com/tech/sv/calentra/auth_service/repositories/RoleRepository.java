package com.tech.sv.calentra.auth_service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tech.sv.calentra.auth_service.entities.Role;

public interface RoleRepository extends JpaRepository<Role, UUID>{
	Optional<Role> findByNameIgnoreCase(String name);
}
