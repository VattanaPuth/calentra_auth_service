package valio.auth_service.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DigitsSender {
	
	private final JavaMailSender javaMailSender;
	
	public void sendDigits(String to, String digits) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom("noreply@valio.com");
        mailMessage.setTo(to);
        mailMessage.setSubject("Reset your password");
        mailMessage.setText("Your verification code is: " + digits);

        javaMailSender.send(mailMessage);
    }
}
