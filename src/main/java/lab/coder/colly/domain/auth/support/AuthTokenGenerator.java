package lab.coder.colly.domain.auth.support;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * 인증 토큰 생성을 담당하는 유틸리티.
 */
public final class AuthTokenGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * 유틸리티 클래스 인스턴스 생성을 방지한다.
     */
    private AuthTokenGenerator() {
    }

    /**
     * 지정 바이트 길이의 URL-safe 랜덤 토큰을 생성한다.
     *
     * @param bytes 랜덤 바이트 길이
     * @return URL-safe Base64 토큰 문자열
     */
    public static String randomToken(int bytes) {
        byte[] value = new byte[bytes];
        SECURE_RANDOM.nextBytes(value);
        return Base64
                .getUrlEncoder()
                .withoutPadding()
                .encodeToString(value);
    }
}
