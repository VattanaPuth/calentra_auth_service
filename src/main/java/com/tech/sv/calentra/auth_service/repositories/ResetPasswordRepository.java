package com.tech.sv.calentra.auth_service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tech.sv.calentra.auth_service.entities.Register;
import com.tech.sv.calentra.auth_service.entities.ResetPassword;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword, UUID>{
	Optional<ResetPassword> findTopByRegisterAndTokenIsFalse(Register register);
}
