package lab.coder.colly.domain.auth.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 인증 도메인 사용자 정보를 표현하는 도메인 모델.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthUser {

    private final Long id;
    private final String email;
    private final String name;

    /**
     * 신규 인증 사용자를 생성한다.
     *
     * @param email 사용자 이메일
     * @param name  사용자 이름
     * @return 신규 인증 사용자
     */
    public static AuthUser create(
            String email,
            String name
    ) {
        return new AuthUser(
                null,
                email,
                name
        );
    }

    /**
     * 저장된 인증 사용자를 복원한다.
     *
     * @param id    식별자
     * @param email 사용자 이메일
     * @param name  사용자 이름
     * @return 복원된 인증 사용자
     */
    public static AuthUser restore(
            Long id,
            String email,
            String name
    ) {
        return new AuthUser(
                id,
                email,
                name
        );
    }
}
