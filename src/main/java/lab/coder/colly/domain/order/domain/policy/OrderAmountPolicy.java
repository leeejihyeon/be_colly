package lab.coder.colly.domain.order.domain.policy;

import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;

import java.math.BigDecimal;

/**
 * 주문 금액 유효성 규칙을 검증하는 도메인 정책 클래스이다.
 */
public final class OrderAmountPolicy {

    /**
     * 인스턴스 생성을 막기 위한 기본 생성자이다.
     */
    private OrderAmountPolicy() {
    }

    /**
     * 주문 금액이 유효한지 검증한다.
     *
     * @param amount 검증할 주문 금액
     * @throws DomainException 금액이 null이거나 0 이하인 경우
     */
    public static void validate(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new DomainException(
                    ErrorCode.INVALID_AMOUNT,
                    "Order amount must be greater than 0"
            );
        }
    }
}
