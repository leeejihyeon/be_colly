package lab.coder.colly.domain.auth.application.port.out;

/**
 * 매직링크 메일 발송 포트.
 */
public interface AuthMagicLinkMailPort {

    /**
     * 매직링크 메일을 발송한다.
     *
     * @param email    수신자 이메일
     * @param token    매직링크 원문 토큰
     * @param expireIn 만료 시간(초)
     */
    void sendMagicLink(
            String email,
            String token,
            long expireIn
    );
}
