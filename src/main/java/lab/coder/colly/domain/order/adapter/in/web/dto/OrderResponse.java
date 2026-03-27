package lab.coder.colly.domain.order.adapter.in.web.dto;

import java.math.BigDecimal;
import lab.coder.colly.domain.order.application.port.in.OrderView;

/**
 * 주문 응답 DTO.
 *
 * @param id 주문 식별자
 * @param userId 주문자 사용자 ID
 * @param amount 주문 금액
 */
public record OrderResponse(Long id, Long userId, BigDecimal amount) {

    /**
     * 주문 뷰를 응답 DTO로 변환한다.
     *
     * @param view 주문 조회/생성 결과 뷰
     * @return 주문 응답 DTO
     */
    public static OrderResponse from(OrderView view) {
        return new OrderResponse(view.id(), view.userId(), view.amount());
    }
}
