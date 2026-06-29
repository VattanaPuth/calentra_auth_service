package com.tech.sv.calentra.auth_service.utils;

import com.tech.sv.calentra.auth_service.entities.Register;
import com.tech.sv.calentra.auth_service.repositories.RegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
                .authorities(register.getRole().getAuthorities()) // ROLE & PERMISSION
                .isAccountNonExpired(Boolean.TRUE.equals(register.getIsAccountNonExpired()))
                .isAccountNonLocked(register.getIsAccountNonLocked())
                .isCredentialsNonExpired(register.getIsCredentialsNonExpired())
                .isEnabled(register.getIsEnabled())
                .build();
    }
}
