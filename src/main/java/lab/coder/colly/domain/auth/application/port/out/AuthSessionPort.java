package lab.coder.colly.domain.auth.application.port.out;

import lab.coder.colly.domain.auth.domain.model.AuthSession;

/**
 * 인증 세션 저장 아웃바운드 포트.
 */
public interface AuthSessionPort {

    /**
     * 인증 세션을 저장한다.
     *
     * @param session 저장할 인증 세션
     * @return 저장된 인증 세션
     */
    AuthSession save(AuthSession session);
}
