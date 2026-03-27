package lab.coder.colly.domain.community.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 모임 참여 방식을 정의하는 enum이다.
 */
@Getter
@RequiredArgsConstructor
public enum JoinPolicy {
    APPROVAL("승인형", "Approval"),
    FREE("자유참여형", "Free");

    private final String koLabel;
    private final String engLabel;
}
