package valio.auth_service.dtos.responses;

import java.util.UUID;

public record PermissionResponseDTO(UUID id, String name, String description) {}
