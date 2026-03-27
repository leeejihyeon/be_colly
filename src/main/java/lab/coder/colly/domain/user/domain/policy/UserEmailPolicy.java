package lab.coder.colly.domain.user.domain.policy;

import java.util.regex.Pattern;
import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;

public final class UserEmailPolicy {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private UserEmailPolicy() {
    }

    public static String normalizeAndValidate(String rawEmail) {
        if (rawEmail == null) {
            throw new DomainException(ErrorCode.DUPLICATE_EMAIL, "Email must not be null");
        }
        String normalized = rawEmail.trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new DomainException(ErrorCode.DUPLICATE_EMAIL, "Email format is invalid");
        }
        return normalized;
    }
}
