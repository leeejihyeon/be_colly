package lab.coder.colly.domain.user.application.port.in;

/**
 * 사용자 생성 유스케이스.
 */
public interface CreateUserUseCase {

    /**
     * 사용자를 생성한다.
     *
     * @param command 사용자 생성 명령
     * @return 생성된 사용자 뷰
     */
    UserView create(CreateUserCommand command);

    /**
     * 사용자 생성 명령.
     *
     * @param email 사용자 이메일
     * @param name  사용자 이름
     */
    record CreateUserCommand(
            String email,
            String name
    ) {
    }
}
