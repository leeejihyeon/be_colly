package lab.coder.colly.domain.user.adapter.out.persistence;

import java.util.Optional;
import lab.coder.colly.domain.user.adapter.out.persistence.mapper.UserPersistenceMapper;
import lab.coder.colly.domain.user.adapter.out.persistence.repository.UserJpaRepository;
import lab.coder.colly.domain.user.application.port.out.UserRepositoryPort;
import lab.coder.colly.domain.user.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    public UserPersistenceAdapter(UserJpaRepository userJpaRepository, UserPersistenceMapper userPersistenceMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userPersistenceMapper = userPersistenceMapper;
    }

    @Override
    public User save(User user) {
        return userPersistenceMapper.toDomain(userJpaRepository.save(userPersistenceMapper.toEntity(user)));
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId).map(userPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
