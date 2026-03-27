package lab.coder.colly.domain.user.application.port.out;

import java.util.Optional;
import lab.coder.colly.domain.user.domain.model.User;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findById(Long userId);

    boolean existsByEmail(String email);
}
