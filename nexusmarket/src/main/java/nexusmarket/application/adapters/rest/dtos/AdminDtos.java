package nexusmarket.application.adapters.rest.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Map;

public final class AdminDtos {

    private AdminDtos() {
    }

    public record UserRequest(
            @NotBlank @Size(max = 120) String fullName,
            @NotBlank @Email @Size(max = 120) String email,
            @NotBlank @Size(max = 60) String username,
            @NotBlank @Size(min = 6, max = 100) String password,
            @NotBlank String role) {
    }

    public record UpdateUserRequest(
            @NotBlank String id,
            @Size(max = 120) String fullName,
            @Email @Size(max = 120) String email,
            String role) {
    }

    public record UserResponse(String id, String fullName, String email, String username, String role, String state) {
    }

    public record AuditLogResponse(String auditId, String operationType, LocalDateTime operationDate,
                                   String performedBy, String role, String affectedType, String affectedId,
                                   Map<String, Object> details) {
    }
}
