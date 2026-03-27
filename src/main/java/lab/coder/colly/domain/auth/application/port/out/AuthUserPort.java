package lab.coder.colly.domain.auth.application.port.out;

import java.util.Optional;
import lab.coder.colly.domain.auth.domain.model.AuthUser;

public interface AuthUserPort {

    Optional<AuthUser> findByEmail(String email);

    AuthUser save(AuthUser user);
}
