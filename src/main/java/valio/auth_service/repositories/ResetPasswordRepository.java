package valio.auth_service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import valio.auth_service.entities.Register;
import valio.auth_service.entities.ResetPassword;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword, UUID>{
	Optional<ResetPassword> findTopByRegisterAndTokenIsFalse(Register register);
}
