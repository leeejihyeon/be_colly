package lab.coder.colly.domain.user.application.service;

import lab.coder.colly.domain.user.application.port.in.CreateUserUseCase;
import lab.coder.colly.domain.user.application.port.in.GetUserUseCase;
import lab.coder.colly.domain.user.application.port.in.UserView;
import lab.coder.colly.domain.user.application.port.out.UserRepositoryPort;
import lab.coder.colly.domain.user.domain.model.User;
import lab.coder.colly.domain.user.domain.policy.UserEmailPolicy;
import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService implements CreateUserUseCase, GetUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public UserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    /**
     * 사용자를 생성한다.
     *
     * @param command 사용자 생성 명령
     * @return 생성된 사용자 응답 뷰
     */
    @Override
    @Transactional
    public UserView create(CreateUserCommand command) {
        String email = UserEmailPolicy.normalizeAndValidate(command.email());
        if (userRepositoryPort.existsByEmail(email)) {
            throw new DomainException(ErrorCode.DUPLICATE_EMAIL, "Email already exists");
        }

        User created = userRepositoryPort.save(User.create(email, command.name()));
        return toView(created);
    }

    /**
     * 사용자 식별자로 단건 조회한다.
     *
     * @param userId 사용자 식별자
     * @return 조회된 사용자 응답 뷰
     */
    @Override
    public UserView getById(Long userId) {
        User user = userRepositoryPort.findById(userId)
            .orElseThrow(() -> new DomainException(ErrorCode.USER_NOT_FOUND, "User not found: " + userId));
        return toView(user);
    }

    /**
     * 도메인 사용자 모델을 응답 뷰로 변환한다.
     *
     * @param user 도메인 사용자
     * @return 사용자 응답 뷰
     */
    private UserView toView(User user) {
        return new UserView(user.getId(), user.getEmail(), user.getName());
    }
}
