package lab.coder.colly.domain.community.application.port.in;

import java.time.LocalDateTime;

/**
 * 사용자 제재 상태 조회 유스케이스.
 */
public interface GetRestrictionUseCase {

    /**
     * 사용자 활성 제재 정보를 조회한다.
     *
     * @param userId 사용자 식별자
     * @return 제재 상태 정보
     */
    RestrictionView getActiveRestriction(Long userId);

    /**
     * 제재 상태 뷰.
     *
     * @param active 활성 제재 여부
     * @param startAt 제재 시작 시각
     * @param endAt 제재 종료 시각
     * @param reason 제재 사유
     */
    record RestrictionView(boolean active, LocalDateTime startAt, LocalDateTime endAt, String reason) {
    }
}
