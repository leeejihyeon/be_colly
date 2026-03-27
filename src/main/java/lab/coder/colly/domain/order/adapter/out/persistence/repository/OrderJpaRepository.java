package lab.coder.colly.domain.order.adapter.out.persistence.repository;

import lab.coder.colly.domain.order.adapter.out.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
}
