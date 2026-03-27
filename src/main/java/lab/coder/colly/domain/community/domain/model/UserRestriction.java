package lab.coder.colly.domain.community.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 사용자 제재(이용 제한) 도메인 모델이다.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRestriction {

    private final Long id;
    private final Long userId;
    private final RestrictionType type;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final String reason;

    /**
     * 신규 사용자 제재 도메인 객체를 생성한다.
     *
     * @param userId  제재 대상 사용자 ID
     * @param type    제재 유형
     * @param startAt 제재 시작 시각
     * @param endAt   제재 종료 시각
     * @param reason  제재 사유
     * @return 신규 사용자 제재 도메인 객체
     */
    public static UserRestriction create(
            Long userId,
            RestrictionType type,
            LocalDateTime startAt,
            LocalDateTime endAt,
            String reason
    ) {
        return new UserRestriction(
                null, userId, type, startAt, endAt, reason);
    }

    /**
     * 저장소 데이터로 사용자 제재 도메인 객체를 복원한다.
     *
     * @param id      제재 ID
     * @param userId  제재 대상 사용자 ID
     * @param type    제재 유형
     * @param startAt 제재 시작 시각
     * @param endAt   제재 종료 시각
     * @param reason  제재 사유
     * @return 복원된 사용자 제재 도메인 객체
     */
    public static UserRestriction restore(
            Long id,
            Long userId,
            RestrictionType type,
            LocalDateTime startAt,
            LocalDateTime endAt,
            String reason
    ) {
        return new UserRestriction(
                id,
                userId,
                type,
                startAt,
                endAt,
                reason
        );
    }

    /**
     * 지정 시각이 제재 기간에 포함되는지 확인한다.
     *
     * @param now 확인 시각
     * @return 제재 활성 상태 여부
     */
    public boolean isActiveAt(LocalDateTime now) {
        return !now.isBefore(startAt) && !now.isAfter(endAt);
    }
}
