package com.tech.sv.calentra.auth_service.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tech.sv.calentra.auth_service.enums.OAuth2Providers;
import com.tech.sv.calentra.auth_service.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Register extends JpaAuditor {

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.UUID) // aaaa-bbbb-cccc-dddd, 1, 2, 3, 4
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = true)
    private String password;
    
    @Enumerated(EnumType.STRING)
    private OAuth2Providers provider;
    
    private String providerId;          
    private String avatarUrl;
    private Boolean emailVerified;

    @Column(nullable = false)
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    private LocalDateTime lockUntil;

    @Builder.Default
    private Boolean isAccountNonExpired = true;

    @Builder.Default
    private Boolean isAccountNonLocked = true;

    @Builder.Default
    private Boolean isCredentialsNonExpired = true;

    @Builder.Default
    private Boolean isEnabled = true;
}
