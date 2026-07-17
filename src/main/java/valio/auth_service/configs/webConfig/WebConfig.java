package valio.auth_service.configs.webConfig;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import valio.auth_service.filters.CsrfCookieFilter;
import valio.auth_service.filters.JwtLoginFilter;
import valio.auth_service.filters.JwtVerifyFilter;
import valio.auth_service.repositories.RegisterRepository;
import valio.auth_service.strategies.Jwt.impl.AttemptsValidationStrategy;
import valio.auth_service.strategies.Jwt.impl.AuthHeaderValidationStrategy;
import valio.auth_service.strategies.Jwt.impl.ContentLengthValidationStrategy;
import valio.auth_service.strategies.Jwt.impl.UsernamePasswordValidationStrategy;
import valio.auth_service.utils.AccessTokenProvider;
import valio.auth_service.utils.RefreshTokenProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebConfig {

    private final UserDetailsService userDetailsService;
    private final ContentLengthValidationStrategy contentLengthValidation;
    private final UsernamePasswordValidationStrategy usernamePasswordValidation;
    private final AttemptsValidationStrategy attemptsValidation;
    private final RegisterRepository registerRepository;
    private final AuthHeaderValidationStrategy tokenExtractor;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider  refreshTokenProvider;
    private final AuthenticationSuccessHandler OAuth2LoginSuccessHandlerServiceImpl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corConfigurationSource()))
                .csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository())
                				  .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(getAuthenticationProvider())
                .authorizeHttpRequests(rq -> rq
                		.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                		.requestMatchers(HttpMethod.GET, "/auth/csrf").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register", "/auth/login", "/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/logout").access(new WebExpressionAuthorizationManager("isAuthenticated()"))
                        .anyRequest().authenticated())
                .addFilterBefore(jwtVerifyFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(jwtLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(OAuth2LoginSuccessHandlerServiceImpl)
                        .failureHandler((request, response, exception) -> {
							response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.getWriter().write(
                            		"""
	                                    {
	                                      "status": "UNAUTHORIZED",
	                                      "message": "OAuth2 authentication failed."
	                                    }
                                    """);
                        })
                )
                .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write(
                    	"""
	                        {
	                            "status": "UNAUTHORIZED",
	                            "message": "Please sign in to continue."
	                        }
                        """);
                }))
                .build();
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
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public JwtLoginFilter jwtLoginFilter() throws Exception {
        JwtLoginFilter filter =  new JwtLoginFilter(
	        		contentLengthValidation, 
	        		usernamePasswordValidation, 
	        		attemptsValidation, 
	        		registerRepository, 
	        		authenticationManager(), 
	        		accessTokenProvider, 
	        		refreshTokenProvider
        		);

        filter.setFilterProcessesUrl("/auth/login");
        return filter;
    }

    @Bean
    public JwtVerifyFilter jwtVerifyFilter(){
        return new JwtVerifyFilter(tokenExtractor);
    }
    
    @Bean
    public CookieCsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = new CookieCsrfTokenRepository();

        repository.setCookieName("XSRF-TOKEN");
        repository.setHeaderName("X-XSRF-TOKEN");
        repository.setCookiePath("/");

        repository.setCookieCustomizer(cookie -> cookie
            .httpOnly(false)
            .secure(false)
            .sameSite("Lax")
            .path("/")
        );

        return repository;
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
