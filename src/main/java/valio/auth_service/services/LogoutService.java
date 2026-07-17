package valio.auth_service.services;

import java.util.Map;

public interface LogoutService {
    Map<String, String> logout(String refreshToken);
}
