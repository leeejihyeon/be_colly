package lab.coder.colly.domain.order.adapter.out.persistence;

import java.util.Optional;
import lab.coder.colly.domain.order.adapter.out.persistence.mapper.OrderPersistenceMapper;
import lab.coder.colly.domain.order.adapter.out.persistence.repository.OrderJpaRepository;
import lab.coder.colly.domain.order.application.port.out.OrderRepositoryPort;
import lab.coder.colly.domain.order.domain.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderPersistenceAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderPersistenceMapper orderPersistenceMapper;

    public OrderPersistenceAdapter(OrderJpaRepository orderJpaRepository, OrderPersistenceMapper orderPersistenceMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderPersistenceMapper = orderPersistenceMapper;
    }

    @Override
    public Order save(Order order) {
        return orderPersistenceMapper.toDomain(orderJpaRepository.save(orderPersistenceMapper.toEntity(order)));
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderJpaRepository.findById(orderId).map(orderPersistenceMapper::toDomain);
    }
}
