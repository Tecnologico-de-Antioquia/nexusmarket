package nexusmarket.application.infrastructure.security;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import nexusmarket.application.domain.ports.out.PaymentGatewayPort;

@Service
public class MockPaymentGateway implements PaymentGatewayPort {
    @Override
    public boolean authorize(String orderId, BigDecimal amount) {
        return orderId != null && amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public boolean capture(String orderId, BigDecimal amount) {
        return authorize(orderId, amount);
    }
}
