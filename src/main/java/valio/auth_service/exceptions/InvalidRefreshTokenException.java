package valio.auth_service.exceptions;

import lombok.Data;

@Data
public class InvalidRefreshTokenException extends RuntimeException{
    private String message;
}
