package com.tech.sv.calentra.auth_service.configs.authConfig;

import com.tech.sv.calentra.auth_service.filters.JwtLoginFilter;
import com.tech.sv.calentra.auth_service.filters.JwtVerifyFilter;
import com.tech.sv.calentra.auth_service.repositories.RegisterRepository;
import com.tech.sv.calentra.auth_service.services.RefreshTokenService;
import com.tech.sv.calentra.auth_service.strategy.HttpRequestValidateRule.DoFilterInternalValidateRule;
import com.tech.sv.calentra.auth_service.strategy.HttpRequestValidateRule.impl.JwtMaxAttempt;
import com.tech.sv.calentra.auth_service.strategy.HttpRequestValidateRule.impl.JwtValidateContentLength;
import com.tech.sv.calentra.auth_service.strategy.HttpRequestValidateRule.impl.JwtValidateUsernamePassword;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthConfig {

    private final UserDetailsService userDetailsService;
    private final JwtValidateContentLength jwtValidateContentLength;
    private final JwtValidateUsernamePassword jwtValidateUsernamePassword;
    private final JwtMaxAttempt jwtMaxAttempt;
    private final RegisterRepository registerRepository;
    private final RefreshTokenService refreshTokenServiceImpl;
    private final DoFilterInternalValidateRule doFilterInternalValidateRule;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(getAuthenticationProvider())
                .authorizeHttpRequests(rq -> rq
                        .requestMatchers(HttpMethod.POST, "/auth/register", "/auth/login", "/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/logout")
                        .access(new WebExpressionAuthorizationManager("isAuthenticated()"))
                        .anyRequest().authenticated())
                .addFilterBefore(jwtVerifyFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(jwtLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("""
                        {
                            "status": "UNAUTHORIZED",
                            "message": "Please sign in to continue."
                        }
                        """);
                }))
                .build();
    }

    @Bean
    public JwtLoginFilter jwtLoginFilter() throws Exception {
        JwtLoginFilter filter =  new JwtLoginFilter(
                jwtValidateContentLength,
                jwtValidateUsernamePassword,
                jwtMaxAttempt,
                registerRepository,
                refreshTokenServiceImpl,
                getAuthenticationManager()
        );

        filter.setAuthenticationManager(getAuthenticationManager());
        filter.setFilterProcessesUrl("/auth/login");
        return filter;
    }

    @Bean
    public JwtVerifyFilter jwtVerifyFilter(){
        return new JwtVerifyFilter(doFilterInternalValidateRule);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(14);
    }

    @Bean
    public AuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider(userDetailsService);
        dao.setPasswordEncoder(passwordEncoder());
        return dao;
    }

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization", "Content-Disposition"));
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of("*"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return (CorsConfigurationSource) source;
    }
}
