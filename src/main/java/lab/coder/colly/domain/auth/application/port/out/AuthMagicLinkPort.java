package lab.coder.colly.domain.auth.application.port.out;

import lab.coder.colly.domain.auth.domain.model.AuthMagicLink;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 매직링크 인증 정보 저장/조회 아웃바운드 포트.
 */
public interface AuthMagicLinkPort {

    /**
     * 매직링크 인증 정보를 저장한다.
     *
     * @param magicLink 저장할 매직링크 인증 정보
     * @return 저장된 매직링크 인증 정보
     */
    AuthMagicLink save(AuthMagicLink magicLink);

    /**
     * 토큰 해시로 매직링크 인증 정보를 조회한다.
     *
     * @param tokenHash 매직링크 토큰 해시
     * @return 매직링크 인증 정보 조회 결과
     */
    Optional<AuthMagicLink> findByTokenHash(String tokenHash);

    /**
     * 기준 시각 이후 동일 이메일의 발급 이력 존재 여부를 조회한다.
     *
     * @param email     로그인 요청 이메일
     * @param afterTime 기준 시각
     * @return 최근 발급 이력 존재 여부
     */
    boolean existsRecentByEmail(
            String email,
            LocalDateTime afterTime
    );
}
