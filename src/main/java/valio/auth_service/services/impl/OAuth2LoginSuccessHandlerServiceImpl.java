package valio.auth_service.services.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import valio.auth_service.dtos.responses.OAuth2UserInfo;
import valio.auth_service.entities.RefreshToken;
import valio.auth_service.entities.Register;
import valio.auth_service.enums.OAuth2Providers;
import valio.auth_service.factories.Oauth2ProviderStrategyFactory;
import valio.auth_service.repositories.RegisterRepository;
import valio.auth_service.strategies.Oauth2.Oauth2ProvidersStrategy;
import valio.auth_service.utils.AccessTokenProvider;
import valio.auth_service.utils.RefreshTokenProvider;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandlerServiceImpl implements AuthenticationSuccessHandler {

	private final Oauth2ProviderStrategyFactory strategyFactory;
	private final RegisterRepository registerRepository;
	private final AccessTokenProvider accessTokenProvider;
	private final RefreshTokenProvider refreshTokenProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

		String registrationId = extractRegistrationId(authentication);
		OAuth2Providers provider = OAuth2Providers.valueOf(registrationId.toUpperCase());

		Oauth2ProvidersStrategy strategy = strategyFactory.getStrategies(provider);

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
								.providerId(userInfo.providerUserId())
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
