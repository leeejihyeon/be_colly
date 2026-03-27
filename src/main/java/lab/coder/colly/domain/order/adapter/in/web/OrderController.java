package lab.coder.colly.domain.order.adapter.in.web;

import jakarta.validation.Valid;
import lab.coder.colly.domain.order.adapter.in.web.dto.CreateOrderRequest;
import lab.coder.colly.domain.order.adapter.in.web.dto.OrderResponse;
import lab.coder.colly.domain.order.application.port.in.CreateOrderUseCase;
import lab.coder.colly.domain.order.application.port.in.GetOrderUseCase;
import lab.coder.colly.domain.order.application.port.in.OrderView;
import lab.coder.colly.shared.api.ApiResponse;
import lab.coder.colly.shared.api.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;

    /**
     * 주문 생성 API.
     *
     * @param request 주문 생성 요청 바디
     * @return 생성된 주문 응답
     */
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        OrderView view = createOrderUseCase.create(
                new CreateOrderUseCase.CreateOrderCommand(
                        request.userId(),
                        request.amount()
                )
        );
        return ApiResponses.created(OrderResponse.from(view));
    }

    /**
     * 주문 단건 조회 API.
     *
     * @param id 주문 식별자
     * @return 조회된 주문 응답
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getById(@PathVariable Long id) {
        return ApiResponses.ok(OrderResponse.from(getOrderUseCase.getById(id)));
    }
}
