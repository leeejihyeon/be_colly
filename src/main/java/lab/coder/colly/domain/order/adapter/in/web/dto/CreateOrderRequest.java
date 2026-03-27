package lab.coder.colly.domain.order.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 주문 생성 요청 DTO.
 *
 * @param userId 주문자 사용자 ID
 * @param amount 주문 금액
 */
public record CreateOrderRequest(
    @NotNull Long userId,
    @NotNull @DecimalMin("0.01") BigDecimal amount
) {
}
