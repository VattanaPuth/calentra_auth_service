package valio.auth_service.dtos.requests;

import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record RoleRequestDTO(@NotBlank String name, String description, Set<UUID> permissionIds) {
}
