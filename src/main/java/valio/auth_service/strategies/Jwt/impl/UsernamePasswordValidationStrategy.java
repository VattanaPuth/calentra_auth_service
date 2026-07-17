package valio.auth_service.strategies.Jwt.impl;
import org.springframework.stereotype.Service;

import valio.auth_service.dtos.requests.LoginRequestDTO;
import valio.auth_service.exceptions.InvalidUsernamePasswordException;
import valio.auth_service.strategies.Jwt.ValidationRuleStrategy;

@Service
public class UsernamePasswordValidationStrategy implements ValidationRuleStrategy<LoginRequestDTO> {
    @Override
    public void validate(LoginRequestDTO loginRequest) {
        if(loginRequest.getEmail() == null ||
                loginRequest.getEmail().isEmpty() ||
                loginRequest.getPassword() == null ||
                loginRequest.getPassword().isEmpty()) {
            throw new InvalidUsernamePasswordException();
        }
    }
}
