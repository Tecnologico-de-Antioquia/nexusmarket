package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.exceptions.InvalidWarehouseException;
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

    public void validate() {
        if (name == null || name.isBlank()) {
            throw new InvalidWarehouseException("El nombre de la bodega es obligatorio.");
        }
        if (capacity != null && capacity < 0) {
            throw new InvalidWarehouseException("La capacidad de la bodega no puede ser negativa.");
        }
        if (address != null) {
            address.validate();
        }
    }

    public boolean isMarketplaceWarehouse() {
        return WarehouseType.MARKETPLACE.equals(this.type);
    }
}
