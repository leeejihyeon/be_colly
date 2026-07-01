package lab.coder.colly.shared.error;

public enum ErrorCode {
    USER_NOT_FOUND("사용자를 찾을 수 없음", "User Not Found"),
    ORDER_NOT_FOUND("주문을 찾을 수 없음", "Order Not Found"),
    DUPLICATE_EMAIL("이미 사용 중인 이메일", "Duplicate Email"),
    INVALID_AMOUNT("유효하지 않은 금액", "Invalid Amount"),
    POST_NOT_FOUND("게시글을 찾을 수 없음", "Post Not Found"),
    ACCOMMODATION_NOT_FOUND("숙소를 찾을 수 없음", "Accommodation Not Found"),
    STAY_NOT_FOUND("체류 일정을 찾을 수 없음", "Stay Not Found"),
    JOIN_NOT_FOUND("참여 요청을 찾을 수 없음", "Join Not Found"),
    JOIN_NOT_ALLOWED("참여가 허용되지 않음", "Join Not Allowed"),
    JOIN_ALREADY_EXISTS("이미 참여 요청이 존재함", "Join Already Exists"),
    INVALID_POST_PAYLOAD("게시글 요청 값이 잘못됨", "Invalid Post Payload"),
    INVALID_STAY_DATE_RANGE("체류 일정 날짜 범위가 잘못됨", "Invalid Stay Date Range"),
    DUPLICATE_STAY("이미 동일한 체류 일정이 존재함", "Duplicate Stay"),
    USER_RESTRICTED("사용자 이용 제한 상태", "User Restricted"),
    FORBIDDEN_ACTION("허용되지 않은 작업", "Forbidden Action"),
    AUTH_UNAUTHORIZED("인증이 필요함", "Authentication Required"),
    AUTH_INVALID_ACCESS_TOKEN("유효하지 않은 액세스 토큰", "Invalid Access Token"),
    AUTH_INVALID_EMAIL("유효하지 않은 이메일", "Invalid Email"),
    AUTH_INVALID_PROVIDER("유효하지 않은 소셜 제공자", "Invalid Social Provider"),
    AUTH_INVALID_PROVIDER_USER_ID("유효하지 않은 제공자 사용자 식별자", "Invalid Provider User Id"),
    AUTH_SOCIAL_TOKEN_INVALID("유효하지 않은 소셜 토큰", "Invalid Social Token"),
    MAGIC_LINK_NOT_FOUND("매직링크를 찾을 수 없음", "Magic Link Not Found"),
    MAGIC_LINK_EXPIRED("매직링크 만료됨", "Magic Link Expired"),
    MAGIC_LINK_ALREADY_USED("이미 사용된 매직링크", "Magic Link Already Used"),
    MAGIC_LINK_RATE_LIMITED("매직링크 요청이 너무 잦음", "Magic Link Rate Limited"),
    MAGIC_LINK_EMAIL_SEND_FAILED("매직링크 메일 발송 실패", "Magic Link Email Send Failed"),
    REFRESH_TOKEN_NOT_FOUND("리프레시 토큰을 찾을 수 없음", "Refresh Token Not Found"),
    REFRESH_TOKEN_EXPIRED("리프레시 토큰이 만료됨", "Refresh Token Expired");

    private final String koLabel;
    private final String engLabel;

    ErrorCode(String koLabel, String engLabel) {
        this.koLabel = koLabel;
        this.engLabel = engLabel;
    }

    public String getKoLabel() {
        return koLabel;
    }

    public String getEngLabel() {
        return engLabel;
    }
}
