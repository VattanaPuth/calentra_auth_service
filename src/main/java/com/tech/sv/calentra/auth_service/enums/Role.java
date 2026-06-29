package com.tech.sv.calentra.auth_service.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.tech.sv.calentra.auth_service.enums.Permission.*;

@AllArgsConstructor
public enum Role {
    ADMIN(Set.of(ADMIN_WRITE, ADMIN_READ, ADMIN_UPDATE, ADMIN_DELETE)),
    STAFF(Set.of(STAFF_WRITE, STAFF_READ, STAFF_UPDATE, STAFF_DELETE)),
    GUEST(Set.of(GUEST_WRITE, GUEST_READ, GUEST_UPDATE, GUEST_DELETE)),
    HOUSEKEEPING(Set.of(HOUSEKEEPING_WRITE, HOUSEKEEPING_READ, HOUSEKEEPING_UPDATE, HOUSEKEEPING_DELETE));

    private Set<Permission> permissions;

    public Set<? extends GrantedAuthority> getAuthorities(){
        Set<SimpleGrantedAuthority> grantedAuthorities = this.permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getDescription()))
                .collect(Collectors.toSet());

        SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_" + this.name());
        grantedAuthorities.add(role);

        return grantedAuthorities;
    }
}

