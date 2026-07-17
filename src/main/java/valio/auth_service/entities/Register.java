package valio.auth_service.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import valio.auth_service.enums.OAuth2Providers;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "register_roles",
        joinColumns = @JoinColumn(name = "register_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

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
