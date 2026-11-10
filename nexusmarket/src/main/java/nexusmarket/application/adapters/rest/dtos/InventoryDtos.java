package nexusmarket.application.adapters.rest.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public final class InventoryDtos {

    private InventoryDtos() {
    }

    public record InventoryRequest(@NotBlank String productId, @NotBlank String warehouseId,
                                   @PositiveOrZero int availableQuantity) {
    }

    public record MovementRequest(@NotBlank String productId, @NotBlank String warehouseId, int quantity,
                                  Integer delta) {
    }

    public record InventoryResponse(String id, String productId, String warehouseId, int availableQuantity,
                                    int reservedQuantity, int damagedQuantity, String condition) {
    }
}
