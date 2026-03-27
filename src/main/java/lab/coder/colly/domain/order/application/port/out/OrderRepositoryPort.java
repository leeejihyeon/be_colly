package lab.coder.colly.domain.order.application.port.out;

import java.util.Optional;
import lab.coder.colly.domain.order.domain.model.Order;

public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(Long orderId);
}
