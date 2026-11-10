package nexusmarket.application.adapters.persistence.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_movements")
@Getter
@Setter
@NoArgsConstructor
public class InventoryMovementEntity {

    @Id
    private String id;
    private String inventoryId;
    private String productId;
    private String warehouseId;
    private String type;
    private Integer quantity;
    private LocalDateTime occurredAt;
    private String performedById;
    private String reference;
}
