package valio.auth_service.dtos.responses;

import java.util.Set;
import java.util.UUID;

public record RoleResponseDTO(UUID id, String name, String description, Set<PermissionResponseDTO> permissions) {
}