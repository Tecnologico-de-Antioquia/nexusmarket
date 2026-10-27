package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.Setter;
import nexusmarket.application.domain.valueObjects.WarehouseType;

@Getter
@Setter
public class Warehouse {
    private String id;
    private String name;
    private Address address;
    private Integer capacity;
    private WarehouseType type;
    private User supplier;

    public Warehouse(String id, String name, Address address, Integer capacity, WarehouseType type) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse id is required");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse name is required");
        }
        if (address == null) {
            throw new IllegalArgumentException("Warehouse address is required");
        }
        if (capacity == null || capacity <= 0) {
            throw new IllegalArgumentException("Warehouse capacity must be greater than zero");
        }
        if (type == null) {
            throw new IllegalArgumentException("Warehouse type is required");
        }

        this.id = id.trim();
        this.name = name.trim();
        this.address = address;
        this.capacity = capacity;
        this.type = type;
    }
}
