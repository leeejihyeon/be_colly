package lab.coder.colly.domain.community.application.port.out;

import java.time.LocalDateTime;
import java.util.Optional;
import lab.coder.colly.domain.community.domain.model.UserRestriction;

public interface UserRestrictionPort {

    UserRestriction save(UserRestriction restriction);

    Optional<UserRestriction> findActiveByUserId(Long userId, LocalDateTime now);
}
