package lab.coder.colly.domain.auth.support;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 해시 계산을 제공하는 유틸리티.
 */
public final class HashSupport {

    /**
     * 유틸리티 클래스 인스턴스 생성을 방지한다.
     */
    private HashSupport() {
    }

    /**
     * 입력 문자열의 SHA-256 해시를 16진수 문자열로 반환한다.
     *
     * @param input 해시 대상 문자열
     * @return SHA-256 16진수 해시 문자열
     */
    public static String sha256(String input) {
        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder builder = new StringBuilder();

            for (byte b : hash) {
                builder.append(String.format("%02x", b));
            }

            return builder.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}
