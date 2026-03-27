package lab.coder.colly.domain.community.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 커뮤니티 게시글 유형을 정의하는 enum이다.
 */
@Getter
@RequiredArgsConstructor
public enum PostType {
    GATHERING("모임글", "Gathering"),
    FREE_FEED("자유형글", "Free Feed");

    private final String koLabel;
    private final String engLabel;
}
