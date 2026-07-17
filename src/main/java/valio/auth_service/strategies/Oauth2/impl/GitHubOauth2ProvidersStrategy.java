package valio.auth_service.strategies.Oauth2.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import valio.auth_service.dtos.responses.OAuth2UserInfo;
import valio.auth_service.enums.OAuth2Providers;
import valio.auth_service.strategies.Oauth2.Oauth2ProvidersStrategy;

@Component
public class GitHubOauth2ProvidersStrategy implements Oauth2ProvidersStrategy{

	@Override
	public OAuth2Providers provider() {
		return OAuth2Providers.GITHUB;
	}

	@Override
	public OAuth2UserInfo extractUserInfo(Map<String, Object> attributes) {
		String name = (String) attributes.get("name");

		if (name == null || name.isBlank()) {
			name = (String) attributes.get("login");
		}
		return new OAuth2UserInfo(
				(String) attributes.get("id"), 
				(String) attributes.get("email"), 
				name,
				(String) attributes.get("avatar_url"), 
				false
			);
	}
}
