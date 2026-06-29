package lab.coder.colly.domain.auth.application.port.in;

/**
 * 로그아웃 유스케이스.
 */
public interface SignOutUseCase {

    /**
     * 현재 세션을 종료한다.
     *
     * @param command 로그아웃 요청 정보
     */
    void signOut(SignOutCommand command);

    /**
     * 로그아웃 요청 정보.
     *
     * @param refreshToken 종료 대상 리프레시 토큰
     */
    record SignOutCommand(String refreshToken) {
    }
}
