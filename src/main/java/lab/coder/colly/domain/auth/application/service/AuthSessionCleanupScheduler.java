package lab.coder.colly.domain.auth.application.service;

import lab.coder.colly.domain.auth.application.port.out.AuthSessionPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthSessionCleanupScheduler {

    private final AuthSessionPort authSessionPort;

    /**
     * 만료된 인증 세션을 주기적으로 정리한다.
     */
    @Scheduled(cron = "0 */30 * * * *")
    public void cleanupExpiredSessions() {
        try {
            authSessionPort.deleteExpiredSessions();
        } catch (Exception ex) {
            log.warn("Failed to cleanup expired auth sessions", ex);
        }
    }
}
