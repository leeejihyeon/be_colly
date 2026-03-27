package lab.coder.colly.domain.order.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 주문 도메인 모델이다.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Order {

    private final Long id;
    private final Long userId;
    private final BigDecimal amount;

    /**
     * 신규 주문 도메인 객체를 생성한다.
     *
     * @param userId 주문 사용자 ID
     * @param amount 주문 금액
     * @return 신규 주문 도메인 객체
     */
    public static Order create(
            Long userId,
            BigDecimal amount
    ) {
        return new Order(
                null,
                userId,
                amount
        );
    }

    /**
     * 저장소 데이터로 주문 도메인 객체를 복원한다.
     *
     * @param id     주문 ID
     * @param userId 주문 사용자 ID
     * @param amount 주문 금액
     * @return 복원된 주문 도메인 객체
     */
    public static Order restore(
            Long id,
            Long userId,
            BigDecimal amount
    ) {
        return new Order(
                id,
                userId,
                amount
        );
    }
}
