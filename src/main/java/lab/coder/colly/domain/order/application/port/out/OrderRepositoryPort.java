package lab.coder.colly.domain.order.application.port.out;

import java.util.Optional;
import lab.coder.colly.domain.order.domain.model.Order;

/**
 * 주문 저장/조회 아웃바운드 포트.
 */
public interface OrderRepositoryPort {

    /**
     * 주문을 저장한다.
     *
     * @param order 저장할 주문
     * @return 저장된 주문
     */
    Order save(Order order);

    /**
     * 주문 식별자로 주문을 조회한다.
     *
     * @param orderId 주문 식별자
     * @return 주문 조회 결과
     */
    Optional<Order> findById(Long orderId);
}
