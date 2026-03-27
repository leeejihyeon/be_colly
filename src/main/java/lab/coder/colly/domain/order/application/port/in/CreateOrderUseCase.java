package lab.coder.colly.domain.order.application.port.in;

import java.math.BigDecimal;

/**
 * 주문 생성 유스케이스.
 */
public interface CreateOrderUseCase {

    /**
     * 주문을 생성한다.
     *
     * @param command 주문 생성 명령
     * @return 생성된 주문 뷰
     */
    OrderView create(CreateOrderCommand command);

    /**
     * 주문 생성 명령.
     *
     * @param userId 주문자 사용자 ID
     * @param amount 주문 금액
     */
    record CreateOrderCommand(Long userId, BigDecimal amount) {
    }
}
