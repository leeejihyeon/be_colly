package lab.coder.colly.domain.community.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lab.coder.colly.domain.community.domain.model.JoinPolicy;
import lab.coder.colly.domain.community.domain.model.PostType;

import java.time.LocalDateTime;

/**
 * 게시글 생성 요청 DTO.
 *
 * @param authorUserId    작성자 사용자 ID
 * @param cityCode        도시 코드
 * @param type            게시글 타입
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
        @NotNull
        Long authorUserId,

        @NotBlank
        String cityCode,

        @NotNull
        PostType type,

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
