package com.tech.sv.calentra.auth_service.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.tech.sv.calentra.auth_service.dtos.records.OAuth2UserInfo;
import com.tech.sv.calentra.auth_service.entities.RefreshToken;
import com.tech.sv.calentra.auth_service.entities.Register;
import com.tech.sv.calentra.auth_service.enums.OAuth2Providers;
import com.tech.sv.calentra.auth_service.repositories.RegisterRepository;
import com.tech.sv.calentra.auth_service.strategies.Oauth2.Oauth2ProvidersStrategy;
import com.tech.sv.calentra.auth_service.utils.AccessTokenProvider;
import com.tech.sv.calentra.auth_service.utils.RefreshTokenProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandlerService implements AuthenticationSuccessHandler {

	private final List<Oauth2ProvidersStrategy> strategies;
	private final RegisterRepository registerRepository;
	private final AccessTokenProvider accessTokenProvider;
	private final RefreshTokenProvider refreshTokenProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

		String registrationId = extractRegistrationId(authentication);
		OAuth2Providers provider = OAuth2Providers.valueOf(registrationId.toUpperCase());

		Oauth2ProvidersStrategy strategy = strategies.stream()
				.filter(s -> s.provider() == provider)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("No strategy for provider: " + provider));

		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		Map<String, Object> attributes = oAuth2User.getAttributes();
		OAuth2UserInfo userInfo = strategy.extractUserInfo(attributes);

		Register register = findOrCreateRegister(userInfo, provider);

		String accessToken = accessTokenProvider.generateAccessToken(register.getEmail(), authentication.getAuthorities());
		ResponseCookie accessCookie = accessTokenProvider.generateAccessCookie(accessToken);

		RefreshToken refreshToken = refreshTokenProvider.refreshToken(register.getId());
		ResponseCookie refreshCookie = refreshTokenProvider.generateRefreshCookie(refreshToken);

		response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
		response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
		response.sendRedirect("/");
	}

	private Register findOrCreateRegister(OAuth2UserInfo userInfo, OAuth2Providers provider) {
		return registerRepository.findByEmail(userInfo.email())
				.orElseGet(() -> registerRepository.save(
						Register.builder()
								.email(userInfo.email())
								.username(userInfo.name())
								.provider(provider)
								.providerId(userInfo
								.providerUserId())
								.avatarUrl(userInfo.avatarUrl())
								.emailVerified(userInfo.emailVerified())
								.build()));
	}

	private String extractRegistrationId(Authentication authentication) {
		if (authentication instanceof OAuth2AuthenticationToken token) {
			return token.getAuthorizedClientRegistrationId();
		}
		throw new IllegalStateException("Unexpected authentication type: " + authentication.getClass());
	}
}
