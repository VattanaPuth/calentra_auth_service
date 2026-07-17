package valio.auth_service.exceptions;

import lombok.Data;

@Data
public class DuplicateRoleException extends RuntimeException{
	String message;
}
