package nexusmarket.application.domain.ports.out;

import nexusmarket.application.domain.models.Address;

public interface ShipmentProviderPort {
    String createShipment(String orderId, Address originAddress, Address destinationAddress);
    boolean updateTrackingStatus(String shipmentId, String status);
}
