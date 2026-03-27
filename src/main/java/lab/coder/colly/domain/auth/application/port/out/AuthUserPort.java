package lab.coder.colly.domain.auth.application.port.out;

import java.util.Optional;
import lab.coder.colly.domain.auth.domain.model.AuthUser;

/**
 * 인증 도메인 사용자 저장/조회 아웃바운드 포트.
 */
public interface AuthUserPort {

    /**
     * 사용자 식별자로 인증 사용자를 조회한다.
     *
     * @param userId 사용자 식별자
     * @return 인증 사용자 조회 결과
     */
    Optional<AuthUser> findById(Long userId);

    /**
     * 이메일로 인증 사용자를 조회한다.
     *
     * @param email 사용자 이메일
     * @return 인증 사용자 조회 결과
     */
    Optional<AuthUser> findByEmail(String email);

    /**
     * 인증 사용자를 저장한다.
     *
     * @param user 저장할 인증 사용자
     * @return 저장된 인증 사용자
     */
    AuthUser save(AuthUser user);
}
