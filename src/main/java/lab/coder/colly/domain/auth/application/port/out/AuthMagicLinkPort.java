package lab.coder.colly.domain.auth.application.port.out;

import java.time.LocalDateTime;
import java.util.Optional;
import lab.coder.colly.domain.auth.domain.model.AuthMagicLink;

public interface AuthMagicLinkPort {

    AuthMagicLink save(AuthMagicLink magicLink);

    Optional<AuthMagicLink> findByTokenHash(String tokenHash);

    boolean existsRecentByEmail(String email, LocalDateTime afterTime);
}
