package nexusmarket.application.adapters.rest.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class SellerDtos {

    private SellerDtos() {
    }

    public record RegisterRequest(
            @NotBlank @Size(max = 120) String fullName,
            @NotBlank @Email @Size(max = 120) String email,
            @NotBlank @Size(max = 60) String username,
            @NotBlank @Size(min = 6, max = 100) String password,
            @Size(max = 120) String companyName,
            @Size(max = 40) String phoneNumber,
            @Valid WarehouseDtos.WarehouseRequest firstWarehouse) {
    }

    public record UpdateRequest(
            @NotBlank String id,
            @Size(max = 120) String fullName,
            @Email @Size(max = 120) String email,
            @Size(max = 120) String companyName,
            @Size(max = 40) String phoneNumber) {
    }

    public record SellerResponse(String id, String fullName, String email, String username, String companyName,
                                 String phoneNumber, String state) {
    }
}
