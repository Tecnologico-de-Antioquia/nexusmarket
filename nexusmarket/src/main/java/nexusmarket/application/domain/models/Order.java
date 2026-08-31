package nexusmarket.application.domain.models;

import java.util.List;

import nexusmarket.application.domain.valueObjects.OrderState;

public class Order {
    private List<Product> products;
    private Integer idTransaction;
    private Shipment shipment;
    private OrderState state;
}
