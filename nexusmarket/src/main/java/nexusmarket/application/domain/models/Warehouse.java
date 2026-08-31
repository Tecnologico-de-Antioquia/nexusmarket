package nexusmarket.application.domain.models;

import nexusmarket.application.domain.valueObjects.WarehouseType;

public class Warehouse {
    private Integer id;
    private Address address;
    private Integer capacity;
    private WarehouseType type;
    private User supplier;
}
