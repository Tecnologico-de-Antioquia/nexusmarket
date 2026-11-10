package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.exceptions.InvalidShipmentException;
import nexusmarket.application.domain.exceptions.InvalidShipmentStateException;
import nexusmarket.application.domain.valueobjects.ShipmentState;

@Getter
@Setter
@NoArgsConstructor
public class Shipment {

    private String id;
    private Order order;
    private Address originAddress;
    private Address destinationAddress;
    private String trackingGuide;
    private ShipmentState state = ShipmentState.PENDING;

    public void assignTrackingGuide(String trackingGuide) {
        if (trackingGuide == null || trackingGuide.isBlank()) {
            throw new InvalidShipmentException("La guia de envio es obligatoria.");
        }
        this.trackingGuide = trackingGuide;
    }

    public void dispatch() {
        if (!ShipmentState.PENDING.equals(this.state)) {
            throw new InvalidShipmentStateException("Solo un envio pendiente puede despacharse.");
        }
        this.state = ShipmentState.DISPATCHED;
    }

    public void deliver() {
        if (!ShipmentState.DISPATCHED.equals(this.state)) {
            throw new InvalidShipmentStateException("Solo un envio despachado puede entregarse.");
        }
        this.state = ShipmentState.DELIVERED;
    }
}
