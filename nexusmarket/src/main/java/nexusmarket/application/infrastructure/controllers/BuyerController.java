package nexusmarket.application.infrastructure.controllers;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.Address;
import nexusmarket.application.domain.models.Buyer;
import nexusmarket.application.domain.services.address.AddressService;
import nexusmarket.application.domain.services.buyer.BuyerService;
import nexusmarket.application.domain.valueObjects.ComercialState;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerService buyerService;
    private final AddressService addressService;

    @PostMapping("/buyers")
    public Buyer createBuyer(@RequestBody BuyerRequest request) {
        Buyer buyer = new Buyer(request.id(), request.fullName(), request.email());
        buyer.setComercialState(ComercialState.ACTIVE);
        return buyerService.registerBuyer(buyer);
    }

    @GetMapping("/buyers/{id}")
    public Buyer getBuyer(@PathVariable String id) {
        return buyerService.findById(id);
    }

    @PostMapping("/buyers/{id}/addresses")
    public Buyer addAddress(@PathVariable String id, @RequestBody AddressRequest request) {
        Address address = addressService.createAddress(
                request.street(),
                request.city(),
                request.state(),
                request.postalCode(),
                request.country());

        buyerService.addPrimaryAddress(id, address);
        return buyerService.findById(id);
    }

    @GetMapping("/buyers/email/{email}")
    public Optional<Buyer> findByEmail(@PathVariable String email) {
        return buyerService.findByEmail(email);
    }

    public record BuyerRequest(String id, String fullName, String email) {}

    public record AddressRequest(String street, String city, String state, String postalCode, String country) {}
}
