package valio.auth_service.strategies.Oauth2.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import valio.auth_service.dtos.responses.OAuth2UserInfo;
import valio.auth_service.enums.OAuth2Providers;
import valio.auth_service.strategies.Oauth2.Oauth2ProvidersStrategy;

@Component
public class GoogleOauth2ProvidersStrategy implements Oauth2ProvidersStrategy{

	@Override
	public OAuth2Providers provider() {
		return OAuth2Providers.GOOGLE;
	}

	@Override
	public OAuth2UserInfo extractUserInfo(Map<String, Object> attributes) {
		return new OAuth2UserInfo(
						(String) attributes.get("sub"),
						(String) attributes.get("email"),
						(String) attributes.get("name"),
						(String) attributes.get("picture"),
					    Boolean.TRUE.equals(attributes.get("email_verified"))
					);

	}

}
