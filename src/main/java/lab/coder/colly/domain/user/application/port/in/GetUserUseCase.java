package lab.coder.colly.domain.user.application.port.in;

/**
 * 사용자 조회 유스케이스.
 */
public interface GetUserUseCase {

    /**
     * 사용자 식별자로 단건 조회한다.
     *
     * @param userId 사용자 식별자
     * @return 사용자 뷰
     */
    UserView getById(Long userId);
}
