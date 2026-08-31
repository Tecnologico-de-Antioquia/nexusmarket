package nexusmarket.application.domain.valueObjects;

import nexusmarket.application.domain.models.Role;

public final class RolesType extends Role {

    public static final RolesType SELLER = new RolesType("SELLER", "Vendedor", "Vendedor dentro del sistema.");
    public static final RolesType ADMINISTRATOR = new RolesType("ADMINISTRATOR", "Administrador", "Administrador dentro del sistema.");
    public static final RolesType LOGIC_OPERATOR = new RolesType("LOGIC_OPERATOR", "Operador logístico", "Operador logístico dentro del sistema.");
    public static final RolesType SUPERVISOR = new RolesType("SUPERVISOR", "Supervisor", "Supervisor dentro del sistema.");

     private RolesType(String code, String name, String description) {
        super(code, name, description);
    }
}
