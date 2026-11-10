package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.valueobjects.MovementType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class InventoryMovement {

    private String id;
    private Inventory inventory;
    private MovementType type;
    private Integer quantity;
    private LocalDateTime occurredAt = LocalDateTime.now();
    private User performedBy;
    private String reference;
}
