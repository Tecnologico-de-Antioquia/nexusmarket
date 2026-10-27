package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.Setter;
import nexusmarket.application.domain.valueObjects.ShipmentState;

@Getter
@Setter
public class Shipment {
    private String id;
    private String orderId;
    private Address destinationAddress;
    private Address originAddress;
    private String trackingGuide;
    private ShipmentState state;

    public Shipment(String id, String orderId, Address originAddress, Address destinationAddress) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Shipment id is required");
        }
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order id is required");
        }
        if (originAddress == null) {
            throw new IllegalArgumentException("Origin address is required");
        }
        if (destinationAddress == null) {
            throw new IllegalArgumentException("Destination address is required");
        }

        this.id = id.trim();
        this.orderId = orderId.trim();
        this.originAddress = originAddress;
        this.destinationAddress = destinationAddress;
        this.trackingGuide = "TRK-" + id.trim();
        this.state = ShipmentState.PENDING;
    }

    public void dispatch() {
        this.state = ShipmentState.DISPATCHED;
    }

    public void deliver() {
        this.state = ShipmentState.DELIVERED;
    }
}
