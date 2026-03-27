package lab.coder.colly.domain.user.domain.policy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import lab.coder.colly.shared.error.DomainException;
import org.junit.jupiter.api.Test;

class UserEmailPolicyTest {

    @Test
    void normalizeAndValidate_returnsNormalizedEmail() {
        String normalized = UserEmailPolicy.normalizeAndValidate("  TEST@Example.com ");

        assertThat(normalized).isEqualTo("test@example.com");
    }

    @Test
    void normalizeAndValidate_throwsWhenInvalidEmail() {
        assertThatThrownBy(() -> UserEmailPolicy.normalizeAndValidate("invalid-email"))
            .isInstanceOf(DomainException.class);
    }
}
