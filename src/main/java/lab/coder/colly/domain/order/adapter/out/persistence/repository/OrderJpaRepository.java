package lab.coder.colly.domain.order.adapter.out.persistence.repository;

import lab.coder.colly.domain.order.adapter.out.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 주문 엔티티 조회용 JPA 리포지토리.
 */
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
}
