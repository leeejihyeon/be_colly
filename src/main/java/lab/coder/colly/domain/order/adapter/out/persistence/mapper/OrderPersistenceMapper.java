package lab.coder.colly.domain.order.adapter.out.persistence.mapper;

import lab.coder.colly.domain.order.adapter.out.persistence.entity.OrderEntity;
import lab.coder.colly.domain.order.domain.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderPersistenceMapper {

    public OrderEntity toEntity(Order order) {
        return new OrderEntity(order.getId(), order.getUserId(), order.getAmount());
    }

    public Order toDomain(OrderEntity entity) {
        return Order.restore(entity.getId(), entity.getUserId(), entity.getAmount());
    }
}
