package valio.auth_service.strategies.Oauth2;

import java.util.Map;

import valio.auth_service.dtos.responses.OAuth2UserInfo;
import valio.auth_service.enums.OAuth2Providers;

public interface Oauth2ProvidersStrategy {
    OAuth2Providers provider();
    OAuth2UserInfo extractUserInfo(Map<String, Object> attributes);
}
