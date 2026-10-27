package nexusmarket.application.domain.services.address;

import org.springframework.stereotype.Service;

import nexusmarket.application.domain.models.Address;

@Service
public class AddressService {

    public Address createAddress(String street, String city, String state, String postalCode, String country) {
        return new Address(street, city, state, postalCode, country);
    }

    public void validateAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address is required");
        }
        if (address.getStreet() == null || address.getStreet().trim().isEmpty()) {
            throw new IllegalArgumentException("Street is required");
        }
        if (address.getCity() == null || address.getCity().trim().isEmpty()) {
            throw new IllegalArgumentException("City is required");
        }
        if (address.getCountry() == null || address.getCountry().trim().isEmpty()) {
            throw new IllegalArgumentException("Country is required");
        }
    }
}