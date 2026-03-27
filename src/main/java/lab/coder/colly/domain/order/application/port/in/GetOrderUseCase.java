package lab.coder.colly.domain.order.application.port.in;

/**
 * 주문 조회 유스케이스.
 */
public interface GetOrderUseCase {

    /**
     * 주문 식별자로 단건 조회한다.
     *
     * @param orderId 주문 식별자
     * @return 주문 뷰
     */
    OrderView getById(Long orderId);
}
