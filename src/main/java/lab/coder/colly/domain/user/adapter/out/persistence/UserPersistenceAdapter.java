package lab.coder.colly.domain.user.adapter.out.persistence;

import lab.coder.colly.domain.user.adapter.out.persistence.mapper.UserPersistenceMapper;
import lab.coder.colly.domain.user.adapter.out.persistence.repository.UserJpaRepository;
import lab.coder.colly.domain.user.application.port.out.UserRepositoryPort;
import lab.coder.colly.domain.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 사용자 도메인의 영속성 어댑터.
 */
@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    /**
     * 사용자 도메인 모델을 저장한다.
     *
     * @param user 저장할 사용자
     * @return 저장된 사용자
     */
    @Override
    public User save(User user) {
        return userPersistenceMapper.toDomain(
                userJpaRepository.save(
                        userPersistenceMapper.toEntity(user)
                )
        );
    }

    /**
     * 사용자 식별자로 사용자를 조회한다.
     *
     * @param userId 사용자 식별자
     * @return 사용자 조회 결과
     */
    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId)
                .map(userPersistenceMapper::toDomain);
    }

    /**
     * 이메일 중복 여부를 조회한다.
     *
     * @param email 사용자 이메일
     * @return 이메일 중복 여부
     */
    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
