package valio.auth_service.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResourceNotFoundException extends RuntimeException{
    private String message;
}

