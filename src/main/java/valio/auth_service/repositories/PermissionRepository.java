package valio.auth_service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import valio.auth_service.entities.Permission;

public interface PermissionRepository extends JpaRepository<Permission, UUID>{
	Optional<Permission> findByNameIgnoreCase(String name);
	boolean existsByName(String name);

    @Query("SELECT COUNT(r) > 0 FROM Role r JOIN r.permissions p WHERE p.id = :permissionId")
    boolean isAttachedToAnyRole(@Param("permissionId") UUID permissionId);
}
