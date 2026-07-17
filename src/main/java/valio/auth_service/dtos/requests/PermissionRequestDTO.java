package valio.auth_service.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record PermissionRequestDTO(@NotBlank String name, String description) {
}

