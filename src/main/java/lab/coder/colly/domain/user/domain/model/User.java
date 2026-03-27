package lab.coder.colly.domain.user.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 사용자 도메인 모델이다.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    private final Long id;
    private final String email;
    private final String name;

    /**
     * 신규 사용자 도메인 객체를 생성한다.
     *
     * @param email 사용자 이메일
     * @param name  사용자 이름
     * @return 신규 사용자 도메인 객체
     */
    public static User create(
            String email,
            String name
    ) {
        return new User(
                null,
                email,
                name
        );
    }

    /**
     * 저장소 데이터로 사용자 도메인 객체를 복원한다.
     *
     * @param id    사용자 ID
     * @param email 사용자 이메일
     * @param name  사용자 이름
     * @return 복원된 사용자 도메인 객체
     */
    public static User restore(
            Long id,
            String email,
            String name
    ) {
        return new User(
                id,
                email,
                name
        );
    }

    /**
     * 사용자 ID를 반영한 새 도메인 객체를 반환한다.
     *
     * @param newId 설정할 사용자 ID
     * @return ID가 반영된 사용자 도메인 객체
     */
    public User withId(Long newId) {
        return new User(
                newId,
                email,
                name
        );
    }
}
