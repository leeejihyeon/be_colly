package lab.coder.colly.domain.user.adapter.in.web.dto;

import lab.coder.colly.domain.user.application.port.in.UserView;

/**
 * 사용자 응답 DTO.
 *
 * @param id 사용자 식별자
 * @param email 사용자 이메일
 * @param name 사용자 이름
 */
public record UserResponse(Long id, String email, String name) {

    /**
     * 사용자 뷰를 응답 DTO로 변환한다.
     *
     * @param view 사용자 조회/생성 결과 뷰
     * @return 사용자 응답 DTO
     */
    public static UserResponse from(UserView view) {
        return new UserResponse(view.id(), view.email(), view.name());
    }
}
