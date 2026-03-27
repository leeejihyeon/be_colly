package lab.coder.colly.domain.user.domain.policy;

import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;

import java.util.regex.Pattern;

/**
 * 사용자 이메일 정규화/형식 검증 정책을 정의하는 도메인 정책 클래스이다.
 */
public final class UserEmailPolicy {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    /**
     * 인스턴스 생성을 막기 위한 기본 생성자이다.
     */
    private UserEmailPolicy() {
    }

    /**
     * 이메일을 정규화하고 형식을 검증한다.
     *
     * @param rawEmail 원본 이메일 문자열
     * @return 정규화된 이메일 문자열
     * @throws DomainException 이메일이 null이거나 형식이 올바르지 않은 경우
     */
    public static String normalizeAndValidate(String rawEmail) {

        if (rawEmail == null) {
            throw new DomainException(
                    ErrorCode.DUPLICATE_EMAIL,
                    "Email must not be null"
            );
        }

        String normalized = rawEmail.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new DomainException(
                    ErrorCode.DUPLICATE_EMAIL,
                    "Email format is invalid"
            );
        }

        return normalized;
    }
}
