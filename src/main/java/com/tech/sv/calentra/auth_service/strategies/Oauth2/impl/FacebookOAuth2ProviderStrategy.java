package com.tech.sv.calentra.auth_service.strategies.Oauth2.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.tech.sv.calentra.auth_service.dtos.records.OAuth2UserInfo;
import com.tech.sv.calentra.auth_service.enums.OAuth2Providers;
import com.tech.sv.calentra.auth_service.strategies.Oauth2.Oauth2ProvidersStrategy;

@Component
public class FacebookOAuth2ProviderStrategy implements Oauth2ProvidersStrategy{

	@Override
	public OAuth2Providers provider() {
		return OAuth2Providers.FACEBOOK;
	}

	@Override
	public String nameAttributeKey() {
	    return "id";
	}

	@Override
	public OAuth2UserInfo extractUserInfo(Map<String, Object> attributes) {
        return new OAuth2UserInfo(
                (String) attributes.get("id"),
                (String) attributes.get("email"),
                (String) attributes.get("name"),
                extractPictureUrl(attributes),
                false
        );
	}
	
	private String extractPictureUrl(Map<String, Object> attributes) {
		Object pictureObject = attributes.get("picture");

		if (!(pictureObject instanceof Map<?, ?> picture)) {
			return null;
		}

		Object dataObject = picture.get("data");

		if (!(dataObject instanceof Map<?, ?> data)) {
			return null;
		}

		Object url = data.get("url");

		return url == null ? null : url.toString();
	}

}
