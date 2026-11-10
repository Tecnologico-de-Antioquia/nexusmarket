package nexusmarket.application.domain.valueobjects;

import java.util.List;

public final class UserStatus extends DomainCatalog {

    public static final UserStatus ACTIVE = new UserStatus("ACTIVE", "Activo", "El usuario puede operar en la plataforma.");
    public static final UserStatus BLOCKED = new UserStatus("BLOCKED", "Bloqueado", "El usuario fue bloqueado por un administrador.");
    public static final UserStatus INACTIVE = new UserStatus("INACTIVE", "Inactivo", "El usuario fue desactivado.");

    private UserStatus(String code, String name, String description) {
        super(code, name, description);
    }

    public static List<UserStatus> values() {
        return List.of(ACTIVE, BLOCKED, INACTIVE);
    }

    public static UserStatus fromCode(String code) {
        return values().stream()
                .filter(value -> value.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown UserStatus code: " + code));
    }
}
