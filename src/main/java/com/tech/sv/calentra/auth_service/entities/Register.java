package com.tech.sv.calentra.auth_service.entities;

import com.tech.sv.calentra.auth_service.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
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

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Integer failedLoginAttempts = 0;
    private LocalDateTime lockUntil;

    private Boolean isAccountNonExpired = true;
    private Boolean isAccountNonLocked = true;
    private Boolean isCredentialsNonExpired = true;
    private Boolean isEnabled = true;
}
