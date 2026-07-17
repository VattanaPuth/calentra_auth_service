package valio.auth_service.filters;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import valio.auth_service.dtos.requests.LoginRequestDTO;
import valio.auth_service.entities.RefreshToken;
import valio.auth_service.entities.Register;
import valio.auth_service.exceptions.ResourceNotFoundException;
import valio.auth_service.repositories.RegisterRepository;
import valio.auth_service.strategies.Jwt.impl.AttemptsValidationStrategy;
import valio.auth_service.strategies.Jwt.impl.ContentLengthValidationStrategy;
import valio.auth_service.strategies.Jwt.impl.UsernamePasswordValidationStrategy;
import valio.auth_service.utils.AccessTokenProvider;
import valio.auth_service.utils.RefreshTokenProvider;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final ContentLengthValidationStrategy contentLengthValidation;
    private final UsernamePasswordValidationStrategy usernamePasswordValidation;
    private final AttemptsValidationStrategy attemptsValidation;
    private final RegisterRepository registerRepository;
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider  refreshTokenProvider;
    
    public JwtLoginFilter(
            ContentLengthValidationStrategy contentLengthValidation,
            UsernamePasswordValidationStrategy usernamePasswordValidation,
            AttemptsValidationStrategy attemptsValidation,
            RegisterRepository registerRepository,
            AuthenticationManager authenticationManager,
            AccessTokenProvider accessTokenProvider,
            RefreshTokenProvider refreshTokenProvider
    ) {
        super(authenticationManager);
        this.contentLengthValidation = contentLengthValidation;
        this.usernamePasswordValidation = usernamePasswordValidation;
        this.attemptsValidation = attemptsValidation;
        this.registerRepository = registerRepository;
        this.accessTokenProvider = accessTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objMapper = new ObjectMapper();
        contentLengthValidation.validate(request);
        try {
            LoginRequestDTO loginRequest = objMapper.readValue(request.getInputStream(), LoginRequestDTO.class);
            usernamePasswordValidation.validate(loginRequest);
            request.setAttribute("LOGIN_EMAIL", loginRequest.getEmail());
            Register register = registerRepository.findByEmail(loginRequest.getEmail()).orElseThrow(ResourceNotFoundException::new);
            attemptsValidation.validate(register);
            Authentication auth = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            return getAuthenticationManager().authenticate(auth); 
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        String email = authResult.getName();
        Register register = registerRepository.findByEmail(email).orElseThrow(ResourceNotFoundException::new);
        attemptsValidation.resetFailedAttempts(register);

        String accessToken = accessTokenProvider.generateAccessToken(authResult.getName(), authResult.getAuthorities());
        ResponseCookie accessCookie = accessTokenProvider.generateAccessCookie(accessToken);

        RefreshToken refreshToken = refreshTokenProvider.refreshToken(register.getId());
        ResponseCookie refreshCookie = refreshTokenProvider.generateRefreshCookie(refreshToken);

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = """
            {
                "message": "Login successful",
                "tokenType": "Bearer"
            }
            """;
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String email = (String) request.getAttribute("LOGIN_EMAIL");

        if (failed instanceof LockedException) {
            response.setStatus(423); // Locked

            response.getWriter().write("""
            {
                "status": "LOCKED",
                "message": "%s"
            }
            """.formatted(failed.getMessage()));

            response.getWriter().flush();
            return;
        }

        if (email != null) {
            registerRepository.findByEmail(email).ifPresent(attemptsValidation::increaseFailedAttempts);
            Register updatedRegister = registerRepository.findByEmail(email).orElse(null);

            if (updatedRegister != null && Boolean.FALSE.equals(updatedRegister.getIsAccountNonLocked())) {
                response.setStatus(423);

                response.getWriter().write("""
                {
                    "status": "LOCKED",
                    "message": "Too many failed login attempts. Account locked for 15 minutes"
                }
                """);

                response.getWriter().flush();
                return;
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("""
            {
                "status": "UNAUTHORIZED",
                "message": "Invalid email or password"
            }
            """);
        response.getWriter().flush();
    }
}
