package lab.coder.colly.domain.order.application.port.in;

import java.math.BigDecimal;

public record OrderView(Long id, Long userId, BigDecimal amount) {
}
