package nexusmarket.application.domain.models;

import java.util.List;

public class Order {
    private List<Product> products;
    private Integer idTransaction;
    private Shipment shipment;
    private String state;
}
