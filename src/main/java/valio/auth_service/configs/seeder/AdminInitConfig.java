package valio.auth_service.configs.seeder;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import valio.auth_service.entities.Permission;
import valio.auth_service.entities.Register;
import valio.auth_service.entities.Role;
import valio.auth_service.repositories.PermissionRepository;
import valio.auth_service.repositories.RegisterRepository;
import valio.auth_service.repositories.RoleRepository;

@Configuration
@RequiredArgsConstructor
public class AdminInitConfig {
	
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RegisterRepository registerRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Bean
    CommandLineRunner adminInit() {
    	return args -> {
            Permission userCreate = createPermission("USER_CREATE", "Allows creating new user accounts");
            Permission userRead = createPermission("USER_READ", "Allows viewing user account information");
            Permission userUpdate = createPermission("USER_UPDATE", "Allows updating user account information");
            Permission userDisable = createPermission("USER_DISABLE", "Allows disabling or enabling user accounts");

            Permission roleCreate = createPermission("ROLE_CREATE", "Allows creating new roles");
            Permission roleRead = createPermission("ROLE_READ", "Allows viewing roles and their assigned permissions");
            Permission roleUpdate = createPermission("ROLE_UPDATE", "Allows updating role names, descriptions, and permissions");
            Permission roleAssign = createPermission("ROLE_ASSIGN", "Allows assigning roles to users and removing roles from users");

            Permission permissionAssign = createPermission("PERMISSION_ASSIGN", "Allows assigning permissions to roles and removing permissions from roles");

            String getAdminName = "ADMIN";
            String getDescription = "System administrator";
            
            Role adminRole = createRole(getAdminName, getDescription);

            adminRole.getPermissions().addAll(Set.of(
                    userCreate, userRead, userUpdate, userDisable,
                    roleCreate, roleRead, roleUpdate, roleAssign,
                    permissionAssign
            ));
            roleRepository.save(adminRole);
            createInitialAdmin(adminRole);
        };
    }
    
    private Role createRole(String adminRole, String description) {
    	return roleRepository.findByNameIgnoreCase("ADMIN")
                .orElseGet(() -> Role.builder()
	                        .name(adminRole)
	                        .description(description)
	                        .permissions(new HashSet<>())	                        
	                        .build()
                );
    }
    
    private Permission createPermission(String name, String description) {
    	return permissionRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> permissionRepository.save(
                        Permission.builder()
                                  .name(name)
                                  .description(description)
                                  .build()
                ));	
    }
    
    private void createInitialAdmin(Role adminRole) {
        String adminEmail = "admin@valio.com";
        String getUsername = "System Admin";

        if (registerRepository.existsByEmail(adminEmail)) {
            return;
        }

        Register admin = Register.builder()
                .email(adminEmail)
                .username(getUsername)
                .password(passwordEncoder.encode("ChangeMe123!"))
                .roles(new HashSet<>(Set.of(adminRole)))
                .emailVerified(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();

        registerRepository.save(admin);
    }

}
