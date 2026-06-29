package lab.coder.colly.domain.community.domain.policy;

import lab.coder.colly.domain.community.application.port.in.CreateCommunityPostUseCase;
import lab.coder.colly.domain.community.domain.model.GatheringAudienceScope;
import lab.coder.colly.domain.community.domain.model.PostType;
import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;

/**
 * 커뮤니티 게시글 타입별 입력 정책을 검증하는 도메인 정책 클래스이다.
 */
public final class CommunityPostPolicy {

    /**
     * 인스턴스 생성을 막기 위한 기본 생성자이다.
     */
    private CommunityPostPolicy() {
    }

    /**
     * 게시글 타입에 따라 요청 필드 유효성을 검증한다.
     *
     * @param command 게시글 생성 요청 명령
     */
    public static void validateByType(CreateCommunityPostUseCase.CreateCommunityPostCommand command) {
        if (command.type() == PostType.GATHERING) {
            if (isBlank(command.destination())
                    || isBlank(command.meetingPlace())
                    || command.meetingAt() == null
                    || command.maxParticipants() == null
                    || command.joinPolicy() == null
                    || command.audienceScope() == null
            ) {
                throw new DomainException(
                        ErrorCode.INVALID_POST_PAYLOAD,
                        "Gathering post requires destination/meeting/audience fields"
                );
            }

            if (command.audienceScope() == GatheringAudienceScope.ACCOMMODATION_ONLY) {
                if (command.accommodationId() == null
                        || command.audienceStayStartDate() == null
                        || command.audienceStayEndDate() == null
                ) {
                    throw new DomainException(
                            ErrorCode.INVALID_POST_PAYLOAD,
                            "Accommodation-only gathering requires accommodation and stay dates"
                    );
                }

                if (command.audienceStayStartDate().isAfter(command.audienceStayEndDate())) {
                    throw new DomainException(
                            ErrorCode.INVALID_POST_PAYLOAD,
                            "Audience stay start date must be before or equal to end date"
                    );
                }
            }

            if (command.audienceScope() == GatheringAudienceScope.CITY_WIDE) {
                if (command.accommodationId() != null
                        || command.audienceStayStartDate() != null
                        || command.audienceStayEndDate() != null
                ) {
                    throw new DomainException(
                            ErrorCode.INVALID_POST_PAYLOAD,
                            "City-wide gathering cannot include accommodation fields"
                    );
                }
            }
            return;
        }

        if (command.type() == PostType.FREE_FEED) {
            if (command.destination() != null
                    || command.meetingPlace() != null
                    || command.meetingAt() != null
                    || command.maxParticipants() != null
                    || command.joinPolicy() != null
                    || command.audienceScope() != null
                    || command.accommodationId() != null
                    || command.audienceStayStartDate() != null
                    || command.audienceStayEndDate() != null
            ) {
                throw new DomainException(
                        ErrorCode.INVALID_POST_PAYLOAD,
                        "Free feed post cannot include gathering/audience fields"
                );
            }
            return;
        }

        throw new DomainException(
                ErrorCode.INVALID_POST_PAYLOAD,
                "Unsupported post type"
        );
    }

    /**
     * 문자열 공백 여부를 검사한다.
     *
     * @param value 검사할 문자열
     * @return 공백 또는 null 여부
     */
    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
