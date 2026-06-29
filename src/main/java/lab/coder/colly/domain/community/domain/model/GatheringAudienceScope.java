package lab.coder.colly.domain.community.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 모임글 노출 대상을 정의하는 enum이다.
 */
@Getter
@RequiredArgsConstructor
public enum GatheringAudienceScope {
    ACCOMMODATION_ONLY("내 숙소 인원", "My accommodation only"),
    CITY_WIDE("도시 전체", "City-wide");

    private final String koLabel;
    private final String engLabel;
}
