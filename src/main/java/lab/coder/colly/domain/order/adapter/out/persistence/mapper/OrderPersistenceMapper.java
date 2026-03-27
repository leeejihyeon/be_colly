package lab.coder.colly.domain.order.adapter.out.persistence.mapper;

import lab.coder.colly.domain.order.adapter.out.persistence.entity.OrderEntity;
import lab.coder.colly.domain.order.domain.model.Order;
import org.springframework.stereotype.Component;

/**
 * 주문 도메인 모델과 JPA 엔티티 간 변환 매퍼.
 */
@Component
public class OrderPersistenceMapper {

    /**
     * 주문 도메인 모델을 주문 엔티티로 변환한다.
     *
     * @param order 주문 도메인 모델
     * @return 주문 엔티티
     */
    public OrderEntity toEntity(Order order) {
        return new OrderEntity(
                order.getId(),
                order.getUserId(),
                order.getAmount()
        );
    }

    /**
     * 주문 엔티티를 주문 도메인 모델로 변환한다.
     *
     * @param entity 주문 엔티티
     * @return 주문 도메인 모델
     */
    public Order toDomain(OrderEntity entity) {
        return Order.restore(
                entity.getId(),
                entity.getUserId(),
                entity.getAmount()
        );
    }
}
