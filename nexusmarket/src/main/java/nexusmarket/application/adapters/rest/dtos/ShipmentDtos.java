package nexusmarket.application.adapters.rest.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public final class ShipmentDtos {

    private ShipmentDtos() {
    }

    public record ShipmentRequest(@NotBlank String orderId, @Valid BuyerDtos.AddressRequest originAddress,
                                  @Valid BuyerDtos.AddressRequest destinationAddress) {
    }

    public record ShipmentResponse(String id, String orderId, String trackingGuide, String state,
                                   BuyerDtos.AddressResponse originAddress,
                                   BuyerDtos.AddressResponse destinationAddress) {
    }
}
