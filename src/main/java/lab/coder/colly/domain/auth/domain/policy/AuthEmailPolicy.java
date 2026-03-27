package lab.coder.colly.domain.auth.domain.policy;

import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;

import java.util.regex.Pattern;

public final class AuthEmailPolicy {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private AuthEmailPolicy() {
    }

    /**
     * 이메일을 정규화하고 형식을 검증한다.
     *
     * @param rawEmail 원본 이메일 문자열
     * @return 정규화된 이메일(소문자, trim 처리)
     */
    public static String normalizeAndValidate(String rawEmail) {

        if (rawEmail == null) {
            throw new DomainException(
                    ErrorCode.AUTH_INVALID_EMAIL,
                    "Email is required"
            );
        }

        String normalized = rawEmail.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new DomainException(
                    ErrorCode.AUTH_INVALID_EMAIL,
                    "Email format is invalid"
            );
        }

        return normalized;
    }

    /**
     * 이메일을 기반으로 기본 표시 이름을 생성한다.
     *
     * @param email 사용자 이메일
     * @return 기본 표시 이름(골뱅이 앞부분)
     */
    public static String defaultDisplayName(String email) {
        int idx = email.indexOf('@');
        if (idx <= 0) {
            return "traveler";
        }
        return email.substring(0, idx);
    }
}
