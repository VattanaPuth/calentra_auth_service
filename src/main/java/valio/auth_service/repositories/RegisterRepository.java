package valio.auth_service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import valio.auth_service.entities.Register;

public interface RegisterRepository extends JpaRepository<Register, UUID> {
    Optional<Register> findByEmail(String email);
    Boolean existsByEmail(String email);
}
