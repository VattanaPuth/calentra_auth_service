package valio.auth_service.utils;

import java.security.SecureRandom;

public class DigitsGenerator {

	
	private static SecureRandom SECURE_RANDOM = new SecureRandom();
	private static Integer startValue = 100_000;
	private static Integer endValue = 900_000;
	
	public static String digitsGenerator() {
		return String.valueOf(startValue + SECURE_RANDOM.nextInt(endValue));
	}
}

