package com.tech.sv.calentra.auth_service.repositories;

import com.tech.sv.calentra.auth_service.entities.Register;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RegisterRepository extends JpaRepository<Register, UUID> {
    Optional<Register> findByEmail(String email);
}
