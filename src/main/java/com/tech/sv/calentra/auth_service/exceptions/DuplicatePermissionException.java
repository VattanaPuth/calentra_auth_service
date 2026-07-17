package com.tech.sv.calentra.auth_service.exceptions;

import lombok.Data;

@Data
public class DuplicatePermissionException extends RuntimeException{
	private String message;
}
