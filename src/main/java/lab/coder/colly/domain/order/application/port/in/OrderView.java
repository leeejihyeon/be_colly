package lab.coder.colly.domain.order.application.port.in;

import java.math.BigDecimal;

/**
 * 주문 응답 뷰.
 *
 * @param id     주문 식별자
 * @param userId 주문자 사용자 식별자
 * @param amount 주문 금액
 */
public record OrderView(
        Long id,
        Long userId,
        BigDecimal amount
) {
}
