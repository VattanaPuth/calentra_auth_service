package com.tech.sv.calentra.auth_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

import static com.tech.sv.calentra.auth_service.enums.Permission.*;
import static com.tech.sv.calentra.auth_service.enums.Permission.ADMIN_DELETE;

@Getter
@AllArgsConstructor
public enum Permission {

    ADMIN_WRITE("admin:write"),
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),

    STAFF_WRITE("staff:write"),
    STAFF_READ("staff:read"),
    STAFF_UPDATE("staff:update"),
    STAFF_DELETE("staff:delete"),

    GUEST_WRITE("guest:write"),
    GUEST_READ("guest:rwad"),
    GUEST_UPDATE("guest:update"),
    GUEST_DELETE("guest:delete"),

    HOUSEKEEPING_WRITE("housekeeping:write"),
    HOUSEKEEPING_READ("housekeeping:read"),
    HOUSEKEEPING_UPDATE("housekeeping:update"),
    HOUSEKEEPING_DELETE("housekeeping:delete");

    private String description;
}
