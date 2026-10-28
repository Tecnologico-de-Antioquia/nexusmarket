package nexusmarket.application.infrastructure.controllers;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.AuthenticationResult;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.services.user.LoginService;
import nexusmarket.application.domain.valueObjects.RolesType;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;

    @PostMapping("/auth/login")
    public AuthenticationResult login(@RequestBody LoginRequest request) {
        User user = new User(
                UUID.randomUUID().toString(),
                "Marketplace User",
                "user@nexusmarket.com",
                RolesType.ADMINISTRATOR,
                request.username(),
                request.password());

        return loginService.login(user);
    }

    public record LoginRequest(String username, String password) {}
}
