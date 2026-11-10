package nexusmarket.application.adapters.rest.dtos;

import jakarta.validation.constraints.NotBlank;

public final class AuthDtos {

    private AuthDtos() {
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {
    }

    public record UserInfo(String id, String username, String fullName, String email, String role, String state) {
    }

    public record LoginResponse(String token, String tokenType, long expiresIn, UserInfo user) {
    }
}
