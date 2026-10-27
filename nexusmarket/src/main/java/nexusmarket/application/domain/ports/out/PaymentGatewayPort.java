package nexusmarket.application.domain.ports.out;

import java.math.BigDecimal;

public interface PaymentGatewayPort {
    boolean authorize(String orderId, BigDecimal amount);
    boolean capture(String orderId, BigDecimal amount);
}
