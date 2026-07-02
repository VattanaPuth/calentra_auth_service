package com.tech.sv.calentra.auth_service.strategies.Oauth2.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.tech.sv.calentra.auth_service.dtos.records.OAuth2UserInfo;
import com.tech.sv.calentra.auth_service.enums.OAuth2Providers;
import com.tech.sv.calentra.auth_service.strategies.Oauth2.Oauth2ProvidersStrategy;

@Component
public class GoogleOauth2ProvidersStrategy implements Oauth2ProvidersStrategy{

	@Override
	public OAuth2Providers provider() {
		return OAuth2Providers.GOOGLE;
	}

	@Override
	public String nameAttributeKey() {
		return "sub";
	}

	@Override
	public OAuth2UserInfo extractUserInfo(Map<String, Object> attributes) {
		return new OAuth2UserInfo(
						(String) attributes.get("sub"),
						(String) attributes.get("email"),
						(String) attributes.get("name"),
						(String) attributes.get("avatarUrl"),
					    Boolean.TRUE.equals(attributes.get("email_verified"))
					);

	}

}
