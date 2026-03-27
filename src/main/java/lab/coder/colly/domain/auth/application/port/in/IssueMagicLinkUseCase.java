package lab.coder.colly.domain.auth.application.port.in;

/**
 * 매직링크 발급 유스케이스.
 */
public interface IssueMagicLinkUseCase {

    /**
     * 이메일 기반 매직링크 발급을 요청한다.
     *
     * @param command 매직링크 발급 요청 정보
     * @return 매직링크 발급 결과
     */
    MagicLinkIssueResult issue(MagicLinkIssueCommand command);

    /**
     * 매직링크 발급 요청 명령.
     *
     * @param email 로그인 대상 이메일
     */
    record MagicLinkIssueCommand(String email) {
    }

    /**
     * 매직링크 발급 결과.
     *
     * @param email            정규화된 이메일
     * @param expiresInSeconds 토큰 만료 시간(초)
     * @param magicToken       매직링크 검증 토큰
     */
    record MagicLinkIssueResult(
            String email,
            long expiresInSeconds,
            String magicToken
    ) {
    }
}
