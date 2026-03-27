package lab.coder.colly.domain.community.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 모임 참여 요청의 처리 상태를 정의하는 enum이다.
 */
@Getter
@RequiredArgsConstructor
public enum JoinStatus {
    PENDING("대기", "Pending"),
    APPROVED("승인", "Approved"),
    REJECTED("거절", "Rejected"),
    CANCELED("취소", "Canceled");

    private final String koLabel;
    private final String engLabel;
}
