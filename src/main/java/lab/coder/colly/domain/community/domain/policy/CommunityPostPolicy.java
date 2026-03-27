package lab.coder.colly.domain.community.domain.policy;

import lab.coder.colly.domain.community.application.port.in.CreateCommunityPostUseCase;
import lab.coder.colly.domain.community.domain.model.PostType;
import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;

public final class CommunityPostPolicy {

    private CommunityPostPolicy() {
    }

    /**
     * 게시글 타입에 따라 요청 필드 유효성을 검증한다.
     *
     * @param command 게시글 생성 요청 명령
     */
    public static void validateByType(CreateCommunityPostUseCase.CreateCommunityPostCommand command) {
        if (command.type() == PostType.GATHERING) {
            if (isBlank(command.destination()) || isBlank(command.meetingPlace())
                || command.meetingAt() == null || command.maxParticipants() == null || command.joinPolicy() == null) {
                throw new DomainException(ErrorCode.INVALID_POST_PAYLOAD, "Gathering post requires destination/meeting fields");
            }
            return;
        }

        if (command.type() == PostType.FREE_FEED) {
            if (command.destination() != null || command.meetingPlace() != null
                || command.meetingAt() != null || command.maxParticipants() != null || command.joinPolicy() != null) {
                throw new DomainException(ErrorCode.INVALID_POST_PAYLOAD, "Free feed post cannot include gathering fields");
            }
            return;
        }

        throw new DomainException(ErrorCode.INVALID_POST_PAYLOAD, "Unsupported post type");
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
