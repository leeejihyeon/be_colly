package lab.coder.colly.domain.community.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lab.coder.colly.domain.community.domain.model.GatheringAudienceScope;
import lab.coder.colly.domain.community.domain.model.JoinPolicy;
import lab.coder.colly.domain.community.domain.model.PostType;

/**
 * 게시글 생성 요청 DTO.
 *
 * @param countryCode     국가 코드
 * @param cityCode        도시 코드
 * @param type            게시글 타입
 * @param audienceScope   모임글 대상 범위
 * @param accommodationId 숙소 식별자
 * @param audienceStayStartDate 대상 숙박 시작일
 * @param audienceStayEndDate 대상 숙박 종료일
 * @param content         게시글 본문
 * @param imageUrl        게시글 이미지 URL
 * @param locationName    위치 텍스트
 * @param destination     모임 목적지
 * @param meetingPlace    모임 집합 장소
 * @param meetingAt       모임 일시
 * @param maxParticipants 최대 참여 인원
 * @param joinPolicy      참여 정책
 */
public record CreateCommunityPostRequest(
        @NotBlank
        @Size(min = 2, max = 2)
        String countryCode,

        @NotBlank
        String cityCode,

        @NotNull
        PostType type,

        GatheringAudienceScope audienceScope,

        @Positive
        Long accommodationId,

        LocalDate audienceStayStartDate,

        LocalDate audienceStayEndDate,

        @NotBlank
        String content,

        String imageUrl,

        String locationName,

        String destination,

        String meetingPlace,

        LocalDateTime meetingAt,

        @Positive
        Integer maxParticipants,

        JoinPolicy joinPolicy
) {
}
