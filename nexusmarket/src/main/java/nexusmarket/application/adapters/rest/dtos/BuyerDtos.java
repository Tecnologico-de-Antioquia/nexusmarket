package nexusmarket.application.adapters.rest.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public final class BuyerDtos {

    private BuyerDtos() {
    }

    public record RegisterRequest(
            @NotBlank @Size(max = 120) String fullName,
            @NotBlank @Email @Size(max = 120) String email,
            @NotBlank @Size(max = 60) String username,
            @NotBlank @Size(min = 6, max = 100) String password,
            @Valid AddressRequest primaryAddress) {
    }

    public record UpdateRequest(@Size(max = 120) String fullName, @Email @Size(max = 120) String email) {
    }

    public record AddressRequest(
            @NotBlank @Size(max = 200) String street,
            @NotBlank @Size(max = 100) String city,
            @NotBlank @Size(max = 100) String state,
            @NotBlank @Size(max = 20) String postalCode,
            @NotBlank @Size(max = 100) String country,
            @Size(max = 200) String additionalInfo) {
    }

    public record AddressResponse(String id, String street, String city, String state, String postalCode,
                                  String country, String additionalInfo) {
    }

    public record BuyerResponse(String id, String fullName, String email, String username, String commercialState,
                                AddressResponse primaryAddress, List<AddressResponse> additionalAddresses) {
    }
}
