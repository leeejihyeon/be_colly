package lab.coder.colly.domain.auth.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 사용자와 인증 수단(소셜/이메일) 연결 정보를 표현하는 도메인 모델.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthIdentity {

    private final Long id;
    private final Long userId;
    private final AuthProvider provider;
    private final String providerUserId;

    /**
     * 신규 인증 수단 연결 정보를 생성한다.
     *
     * @param userId         사용자 식별자
     * @param provider       인증 제공자
     * @param providerUserId 제공자 사용자 식별자
     * @return 신규 인증 수단 연결 정보
     */
    public static AuthIdentity create(
            Long userId,
            AuthProvider provider,
            String providerUserId
    ) {
        return new AuthIdentity(
                null,
                userId,
                provider,
                providerUserId
        );
    }

    /**
     * 저장된 인증 수단 연결 정보를 복원한다.
     *
     * @param id             인증 수단 연결 식별자
     * @param userId         사용자 식별자
     * @param provider       인증 제공자
     * @param providerUserId 제공자 사용자 식별자
     * @return 복원된 인증 수단 연결 정보
     */
    public static AuthIdentity restore(
            Long id,
            Long userId,
            AuthProvider provider,
            String providerUserId
    ) {
        return new AuthIdentity(
                id,
                userId,
                provider,
                providerUserId
        );
    }
}
