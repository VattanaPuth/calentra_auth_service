package valio.auth_service.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import valio.auth_service.entities.Register;
import valio.auth_service.entities.ResetPassword;
import valio.auth_service.exceptions.ResourceNotFoundException;
import valio.auth_service.repositories.RegisterRepository;
import valio.auth_service.repositories.ResetPasswordRepository;
import valio.auth_service.services.ResetPasswordService;
import valio.auth_service.utils.DigitsGenerator;
import valio.auth_service.utils.DigitsSender;

@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService{
	
	private final RegisterRepository registerRepository;
	private final PasswordEncoder passwordEncoder;
	private final ResetPasswordRepository passwordRepository;
	private final DigitsSender digitsSender;

	@Transactional
	@Override
	public void sendCode(String email) {
		
		Optional<Register> register = registerRepository.findByEmail(email);
		
		if(register.isEmpty()) {
			return;
		}
		
		Register registerEmail = register.get();
		
		String code = DigitsGenerator.digitsGenerator();
		String hashCode = passwordEncoder.encode(code);
		
		ResetPassword reset = new ResetPassword();
		reset.setCode(code);
		reset.setHashCode(hashCode);
		reset.setRegister(registerEmail);
		reset.setExpiredAt(LocalDateTime.now().plusMinutes(2));
		reset.setToken(false);
		passwordRepository.save(reset);
		digitsSender.sendDigits(registerEmail.getEmail(), hashCode);
	}

	@Override
	public Boolean resendCode(String email) {
		
		Optional<Register> register = registerRepository.findByEmail(email);
		
		if(register.isEmpty()) {
			return false;
		}
		
		Register registerEmail = register.get();
		
		Optional<ResetPassword> resetPassword = passwordRepository.findTopByRegisterAndTokenIsFalse(registerEmail);
		
		if(resetPassword.isPresent()) {
			ResetPassword existingToken = resetPassword.get();
			
			if(existingToken.getExpiredAt() != null && existingToken.getExpiredAt().isAfter(LocalDateTime.now())) {
				System.out.println("Verify Code is still valid. Cannot resend yet.");
                return false; 
			}
		}
		
		String newCode = DigitsGenerator.digitsGenerator();
		String newHashCode = passwordEncoder.encode(newCode);
		
		ResetPassword newReset = new ResetPassword();
		newReset.setCode(newCode);
		newReset.setHashCode(newHashCode);
		newReset.setRegister(registerEmail);
		newReset.setExpiredAt(LocalDateTime.now().plusMinutes(2));
		newReset.setToken(false);
		
		passwordRepository.save(newReset);
		digitsSender.sendDigits(registerEmail.getEmail(), newHashCode);
		
		return true;
	}

	@Override
	public void resetPassword(String email, String code, String password) {
		
		Register register = registerRepository.findByEmail(email).orElseThrow(ResourceNotFoundException::new);
		ResetPassword token = passwordRepository.findTopByRegisterAndTokenIsFalse(register).orElseThrow(ResourceNotFoundException::new);
		if(token.getExpiredAt().isBefore(LocalDateTime.now())) throw new RuntimeException("Verify Code Expired");
		if(!passwordEncoder.matches(code, token.getHashCode())) throw new RuntimeException("Invalid Code");
		
		token.setToken(true);
		passwordRepository.save(token);
		register.setPassword(passwordEncoder.encode(password));
		registerRepository.save(register);
		
	}

}
