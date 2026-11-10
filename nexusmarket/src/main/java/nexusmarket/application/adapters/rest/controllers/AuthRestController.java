package nexusmarket.application.adapters.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.rest.dtos.AuthDtos;
import nexusmarket.application.adapters.rest.dtos.BuyerDtos;
import nexusmarket.application.adapters.rest.mappers.RestMapper;
import nexusmarket.application.domain.models.AuthenticationResult;
import nexusmarket.application.domain.models.Buyer;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.in.PublicAccessPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final PublicAccessPort publicAccessPort;

    @PostMapping("/login")
    public ResponseEntity<AuthDtos.LoginResponse> login(@Valid @RequestBody AuthDtos.LoginRequest request) {
        AuthenticationResult result = publicAccessPort.login(RestMapper.toDomain(request));
        return ResponseEntity.ok(RestMapper.toLoginResponse(result));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal(expression = "user") User authenticatedUser) {
        publicAccessPort.logout(authenticatedUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register/buyer")
    public ResponseEntity<BuyerDtos.BuyerResponse> registerBuyer(
            @Valid @RequestBody BuyerDtos.RegisterRequest request) {
        Buyer buyer = publicAccessPort.registerBuyer(RestMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(RestMapper.toBuyerResponse(buyer));
    }
}
