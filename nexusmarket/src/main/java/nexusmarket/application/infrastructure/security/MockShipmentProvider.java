package nexusmarket.application.infrastructure.security;

import org.springframework.stereotype.Service;

import nexusmarket.application.domain.models.Address;
import nexusmarket.application.domain.ports.out.ShipmentProviderPort;

@Service
public class MockShipmentProvider implements ShipmentProviderPort {
    @Override
    public String createShipment(String orderId, Address originAddress, Address destinationAddress) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order id is required");
        }
        return "SHIP-" + orderId;
    }

    @Override
    public boolean updateTrackingStatus(String shipmentId, String status) {
        return shipmentId != null && status != null && !status.trim().isEmpty();
    }
}
