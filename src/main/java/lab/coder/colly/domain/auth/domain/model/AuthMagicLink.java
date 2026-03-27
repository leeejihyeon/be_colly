package lab.coder.colly.domain.auth.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 매직링크 로그인 인증 정보를 표현하는 도메인 모델.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthMagicLink {

    private final Long id;
    private final String email;
    private final String tokenHash;
    private final LocalDateTime expiresAt;
    private final LocalDateTime usedAt;

    /**
     * 신규 매직링크 인증 정보를 생성한다.
     *
     * @param email     로그인 요청 이메일
     * @param tokenHash 매직링크 토큰 해시
     * @param expiresAt 토큰 만료 시각
     * @return 신규 매직링크 인증 정보
     */
    public static AuthMagicLink issue(
            String email,
            String tokenHash,
            LocalDateTime expiresAt
    ) {
        return new AuthMagicLink(
                null,
                email,
                tokenHash,
                expiresAt,
                null
        );
    }

    /**
     * 저장된 매직링크 인증 정보를 복원한다.
     *
     * @param id        식별자
     * @param email     로그인 요청 이메일
     * @param tokenHash 매직링크 토큰 해시
     * @param expiresAt 토큰 만료 시각
     * @param usedAt    토큰 사용 시각
     * @return 복원된 매직링크 인증 정보
     */
    public static AuthMagicLink restore(
            Long id,
            String email,
            String tokenHash,
            LocalDateTime expiresAt,
            LocalDateTime usedAt
    ) {
        return new AuthMagicLink(
                id,
                email,
                tokenHash,
                expiresAt,
                usedAt
        );
    }

    /**
     * 매직링크를 사용 처리한 상태로 갱신한다.
     *
     * @param usedAt 토큰 사용 시각
     * @return 사용 처리된 매직링크 인증 정보
     */
    public AuthMagicLink markUsed(LocalDateTime usedAt) {
        return new AuthMagicLink(
                id,
                email,
                tokenHash,
                expiresAt,
                usedAt
        );
    }

    /**
     * 기준 시각에 매직링크가 만료되었는지 확인한다.
     *
     * @param now 기준 시각
     * @return 만료 여부
     */
    public boolean isExpiredAt(LocalDateTime now) {
        return now.isAfter(expiresAt);
    }

    /**
     * 매직링크가 이미 사용되었는지 확인한다.
     *
     * @return 사용 여부
     */
    public boolean isUsed() {
        return usedAt != null;
    }
}
