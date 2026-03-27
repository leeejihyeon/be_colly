package lab.coder.colly.domain.order.domain.policy;

import java.math.BigDecimal;
import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;

public final class OrderAmountPolicy {

    private OrderAmountPolicy() {
    }

    public static void validate(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new DomainException(ErrorCode.INVALID_AMOUNT, "Order amount must be greater than 0");
        }
    }
}
