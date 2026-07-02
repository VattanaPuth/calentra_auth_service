package com.tech.sv.calentra.auth_service.strategies.Oauth2;

import java.util.Map;

import com.tech.sv.calentra.auth_service.dtos.records.OAuth2UserInfo;
import com.tech.sv.calentra.auth_service.enums.OAuth2Providers;

public interface Oauth2ProvidersStrategy {
    OAuth2Providers provider();
    String nameAttributeKey();
    OAuth2UserInfo extractUserInfo(Map<String, Object> attributes);
}
