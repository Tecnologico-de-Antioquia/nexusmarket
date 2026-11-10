package nexusmarket.application.adapters.external;

import nexusmarket.application.domain.models.Address;
import nexusmarket.application.domain.ports.out.ShipmentProviderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SimulatedShipmentProviderAdapter implements ShipmentProviderPort {

    private static final Logger log = LoggerFactory.getLogger(SimulatedShipmentProviderAdapter.class);

    @Override
    public String createShipment(String orderId, Address originAddress, Address destinationAddress) {
        String guide = "GUIDE-" + UUID.randomUUID();
        log.info("Envio creado (simulado) para el pedido {} con guia {}", orderId, guide);
        return guide;
    }

    @Override
    public boolean updateTrackingStatus(String shipmentId, String status) {
        log.info("Estado de envio {} actualizado (simulado) a {}", shipmentId, status);
        return true;
    }
}
