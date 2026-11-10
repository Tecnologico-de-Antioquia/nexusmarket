package nexusmarket.application.adapters.external;

import nexusmarket.application.domain.ports.out.PaymentGatewayPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SimulatedPaymentGatewayAdapter implements PaymentGatewayPort {

    private static final Logger log = LoggerFactory.getLogger(SimulatedPaymentGatewayAdapter.class);

    @Override
    public boolean authorize(String orderId, BigDecimal amount) {
        log.info("Pago autorizado (simulado) para el pedido {} por {}", orderId, amount);
        return true;
    }

    @Override
    public boolean capture(String orderId, BigDecimal amount) {
        log.info("Pago capturado (simulado) para el pedido {} por {}", orderId, amount);
        return true;
    }

    @Override
    public boolean refund(String orderId, BigDecimal amount) {
        log.info("Reembolso procesado (simulado) para el pedido {} por {}", orderId, amount);
        return true;
    }
}
