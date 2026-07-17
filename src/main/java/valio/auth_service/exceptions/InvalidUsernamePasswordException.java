package valio.auth_service.exceptions;

public class InvalidUsernamePasswordException extends ApiException{
    public InvalidUsernamePasswordException() {
        super("Username and password are required.");
    }
}

