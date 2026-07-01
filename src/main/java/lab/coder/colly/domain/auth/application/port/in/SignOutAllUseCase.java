package lab.coder.colly.domain.auth.application.port.in;

/**
 * 모든 기기 세션 종료 유스케이스.
 */
public interface SignOutAllUseCase {

    /**
     * 현재 사용자의 모든 인증 세션을 종료한다.
     *
     * @param userId 사용자 식별자
     */
    void signOutAll(Long userId);
}
