package nexusmarket.application.domain.valueobjects;

import java.util.List;

public final class SystemRole extends DomainCatalog {

    public static final SystemRole BUYER = new SystemRole("BUYER", "Comprador", "Persona que adquiere productos publicados.");
    public static final SystemRole SELLER = new SystemRole("SELLER", "Vendedor", "Responsable de registrar y administrar sus productos.");
    public static final SystemRole LOGISTIC_OPERATOR = new SystemRole("LOGISTIC_OPERATOR", "Operador logistico", "Encargado de la operacion fisica de bodegas y despachos.");
    public static final SystemRole ADMINISTRATOR = new SystemRole("ADMINISTRATOR", "Administrador", "Responsable de la administracion de vendedores y bodegas.");
    public static final SystemRole SUPERVISOR = new SystemRole("SUPERVISOR", "Supervisor", "Perfil de consulta y seguimiento operativo.");

    private SystemRole(String code, String name, String description) {
        super(code, name, description);
    }

    public static List<SystemRole> values() {
        return List.of(BUYER, SELLER, LOGISTIC_OPERATOR, ADMINISTRATOR, SUPERVISOR);
    }

    public static SystemRole fromCode(String code) {
        return values().stream()
                .filter(value -> value.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown SystemRole code: " + code));
    }
}
