package lab.coder.colly.domain.user.application.port.in;

/**
 * 사용자 응답 뷰.
 *
 * @param id    사용자 식별자
 * @param email 사용자 이메일
 * @param name  사용자 이름
 */
public record UserView(
        Long id,
        String email,
        String name
) {
}
