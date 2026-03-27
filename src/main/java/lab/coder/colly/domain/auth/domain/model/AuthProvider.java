package lab.coder.colly.domain.auth.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 지원하는 인증 제공자 종류를 정의한다.
 */
@Getter
@RequiredArgsConstructor
public enum AuthProvider {
    EMAIL_MAGIC_LINK("이메일 매직링크", "Email Magic Link"),
    GOOGLE("구글", "Google"),
    APPLE("애플", "Apple"),
    FACEBOOK("페이스북", "Facebook");

    private final String koLabel;
    private final String engLabel;
}
