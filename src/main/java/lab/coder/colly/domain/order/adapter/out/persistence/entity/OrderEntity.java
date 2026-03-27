package lab.coder.colly.domain.order.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lab.coder.colly.shared.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    /**
     * 주문 저장용 엔티티를 생성한다.
     *
     * @param id 엔티티 식별자
     * @param userId 주문 사용자 ID
     * @param amount 결제 금액
     */
    public OrderEntity(Long id, Long userId, BigDecimal amount) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
    }
}
