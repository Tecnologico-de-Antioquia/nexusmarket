package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.valueobjects.WarehouseType;

@Getter
@Setter
@NoArgsConstructor
public class Warehouse {

    private String id;
    private String name;
    private Address address;
    private Integer capacity;
    private WarehouseType type;
    private Seller owner;
}
