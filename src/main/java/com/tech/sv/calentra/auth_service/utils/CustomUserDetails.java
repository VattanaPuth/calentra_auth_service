package com.tech.sv.calentra.auth_service.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Builder
public class CustomUserDetails implements UserDetails {

    Set<? extends GrantedAuthority> authorities;
    private String email;
    private String password;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // collection: List, Set, Queue
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { // Expired
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() { // Use to prevent brute force attack
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() { // Credential Expired
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() { // Enable
        return isEnabled;
    }
}
