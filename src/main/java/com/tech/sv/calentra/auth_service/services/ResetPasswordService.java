package com.tech.sv.calentra.auth_service.services;

public interface ResetPasswordService {
	void sendCode(String email);
	Boolean resendCode(String email);
	void resetPassword(String email, String code, String password);
}
