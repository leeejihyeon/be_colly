package lab.coder.colly.domain.auth.application.port.out;

import lab.coder.colly.domain.auth.domain.model.AuthSession;

public interface AuthSessionPort {

    AuthSession save(AuthSession session);
}
