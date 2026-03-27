package lab.coder.colly.domain.order.domain.model;

import java.math.BigDecimal;

public class Order {

    private final Long id;
    private final Long userId;
    private final BigDecimal amount;

    private Order(Long id, Long userId, BigDecimal amount) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
    }

    public static Order create(Long userId, BigDecimal amount) {
        return new Order(null, userId, amount);
    }

    public static Order restore(Long id, Long userId, BigDecimal amount) {
        return new Order(id, userId, amount);
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
