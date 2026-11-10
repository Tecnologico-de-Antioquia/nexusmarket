package nexusmarket.application.adapters.persistence.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventories",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "warehouse_id"}))
@Getter
@Setter
@NoArgsConstructor
public class InventoryEntity {

    @Id
    private String id;
    private String productId;
    private String warehouseId;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private Integer damagedQuantity;
    private String condition;
}
