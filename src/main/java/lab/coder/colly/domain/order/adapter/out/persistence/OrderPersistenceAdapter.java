package lab.coder.colly.domain.order.adapter.out.persistence;

import lab.coder.colly.domain.order.adapter.out.persistence.mapper.OrderPersistenceMapper;
import lab.coder.colly.domain.order.adapter.out.persistence.repository.OrderJpaRepository;
import lab.coder.colly.domain.order.application.port.out.OrderRepositoryPort;
import lab.coder.colly.domain.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 주문 도메인의 영속성 어댑터.
 */
@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderPersistenceMapper orderPersistenceMapper;

    /**
     * 주문 도메인 모델을 저장한다.
     *
     * @param order 저장할 주문
     * @return 저장된 주문
     */
    @Override
    public Order save(Order order) {
        return orderPersistenceMapper.toDomain(
                orderJpaRepository.save(
                        orderPersistenceMapper.toEntity(order)
                )
        );
    }

    /**
     * 주문 식별자로 주문을 조회한다.
     *
     * @param orderId 주문 식별자
     * @return 주문 조회 결과
     */
    @Override
    public Optional<Order> findById(Long orderId) {
        return orderJpaRepository.findById(orderId)
                .map(orderPersistenceMapper::toDomain);
    }
}
