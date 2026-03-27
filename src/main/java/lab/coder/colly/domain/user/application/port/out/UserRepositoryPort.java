package lab.coder.colly.domain.user.application.port.out;

import java.util.Optional;
import lab.coder.colly.domain.user.domain.model.User;

/**
 * 사용자 저장/조회 아웃바운드 포트.
 */
public interface UserRepositoryPort {

    /**
     * 사용자를 저장한다.
     *
     * @param user 저장할 사용자
     * @return 저장된 사용자
     */
    User save(User user);

    /**
     * 사용자 식별자로 사용자를 조회한다.
     *
     * @param userId 사용자 식별자
     * @return 사용자 조회 결과
     */
    Optional<User> findById(Long userId);

    /**
     * 이메일 중복 여부를 조회한다.
     *
     * @param email 사용자 이메일
     * @return 이메일 중복 여부
     */
    boolean existsByEmail(String email);
}
