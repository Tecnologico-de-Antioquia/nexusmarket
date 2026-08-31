package nexusmarket.application.domain.models;

import nexusmarket.application.domain.valueObjects.ShipmentState;

public class Shipment {
    private Address destinationAddress;
    private Address originAddress;
    private String trackingGuide;
    private ShipmentState state;
}
