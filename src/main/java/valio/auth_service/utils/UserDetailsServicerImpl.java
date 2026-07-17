package valio.auth_service.utils;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import valio.auth_service.entities.Permission;
import valio.auth_service.entities.Register;
import valio.auth_service.repositories.RegisterRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServicerImpl implements UserDetailsService {

    private final RegisterRepository registerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Register register = registerRepository.findByEmail(email)
                          .orElseThrow(() -> new RuntimeException("Runtime Exception"));

        return CustomUserDetails.builder()
                .email(register.getEmail())
                .password(register.getPassword())
                .authorities(authorities(register)) // ROLE & PERMISSION
                .isAccountNonExpired(register.getIsAccountNonExpired())
                .isAccountNonLocked(register.getIsAccountNonLocked())
                .isCredentialsNonExpired(register.getIsCredentialsNonExpired())
                .isEnabled(register.getIsEnabled())
                .build();
    }
    
    private Set<GrantedAuthority> authorities(Register register) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        register.getRoles().forEach(role -> {
			String roleName = role.getName().trim().toUpperCase(Locale.ROOT);
			authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));

            role.getPermissions().stream()
                    .map(Permission::getName)
                    .map(String::trim)
                    .map(name -> name.toUpperCase(Locale.ROOT))
                    .map(SimpleGrantedAuthority::new)
                    .forEach(authorities::add);
        });

        return authorities;
    }
}
