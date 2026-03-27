package lab.coder.colly.domain.auth.support;

import java.security.SecureRandom;
import java.util.Base64;

public final class AuthTokenGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private AuthTokenGenerator() {
    }

    public static String randomToken(int bytes) {
        byte[] value = new byte[bytes];
        SECURE_RANDOM.nextBytes(value);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }
}
