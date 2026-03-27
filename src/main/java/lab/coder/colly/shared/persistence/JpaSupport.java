package lab.coder.colly.shared.persistence;

import jakarta.persistence.EntityNotFoundException;
import java.util.function.Supplier;

public final class JpaSupport {

    private JpaSupport() {
    }

    public static <T> T getOrThrow(T value, Supplier<String> messageSupplier) {
        if (value == null) {
            throw new EntityNotFoundException(messageSupplier.get());
        }
        return value;
    }
}
