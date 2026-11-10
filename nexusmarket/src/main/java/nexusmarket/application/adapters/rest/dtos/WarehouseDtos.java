package nexusmarket.application.adapters.rest.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public final class WarehouseDtos {

    private WarehouseDtos() {
    }

    public record WarehouseRequest(@NotBlank String name, @Positive Integer capacity, String ownerId,
                                   @Valid BuyerDtos.AddressRequest address) {
    }

    public record WarehouseResponse(String id, String name, String type, Integer capacity, String ownerId,
                                    BuyerDtos.AddressResponse address) {
    }
}
