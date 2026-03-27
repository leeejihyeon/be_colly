package lab.coder.colly.domain.community.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 사용자 제재 유형을 정의하는 enum이다.
 */
@Getter
@RequiredArgsConstructor
public enum RestrictionType {
    COMMUNITY_ACTIVITY_BAN("커뮤니티 활동 제한", "Community Activity Ban");

    private final String koLabel;
    private final String engLabel;
}
