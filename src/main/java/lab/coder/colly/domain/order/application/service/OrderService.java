package lab.coder.colly.domain.order.application.service;

import lab.coder.colly.domain.order.application.port.in.CreateOrderUseCase;
import lab.coder.colly.domain.order.application.port.in.GetOrderUseCase;
import lab.coder.colly.domain.order.application.port.in.OrderView;
import lab.coder.colly.domain.order.application.port.out.OrderRepositoryPort;
import lab.coder.colly.domain.order.domain.model.Order;
import lab.coder.colly.domain.order.domain.policy.OrderAmountPolicy;
import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주문 유스케이스 구현 서비스.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService implements CreateOrderUseCase, GetOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    /**
     * 주문을 생성한다.
     *
     * @param command 주문 생성 명령
     * @return 생성된 주문 응답 뷰
     */
    @Override
    @Transactional
    public OrderView create(CreateOrderCommand command) {
        OrderAmountPolicy.validate(command.amount());
        Order created = orderRepositoryPort.save(
                Order.create(
                        command.userId(),
                        command.amount()
                )
        );
        return toView(created);
    }

    /**
     * 주문 식별자로 단건 조회한다.
     *
     * @param orderId 주문 식별자
     * @return 조회된 주문 응답 뷰
     */
    @Override
    public OrderView getById(Long orderId) {
        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() ->
                        new DomainException(
                                ErrorCode.ORDER_NOT_FOUND,
                                "Order not found: " + orderId
                        )
                );

        return toView(order);
    }

    /**
     * 도메인 주문 모델을 응답 뷰로 변환한다.
     *
     * @param order 도메인 주문
     * @return 주문 응답 뷰
     */
    private OrderView toView(Order order) {
        return new OrderView(
                order.getId(),
                order.getUserId(),
                order.getAmount()
        );
    }
}
