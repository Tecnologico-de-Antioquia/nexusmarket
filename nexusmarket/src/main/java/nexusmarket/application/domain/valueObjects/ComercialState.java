package nexusmarket.application.domain.valueObjects;

public final class ComercialState {
    private final String code;
    private final String name;
    private final String description;


    private ComercialState(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public static final ComercialState ACTIVE = new ComercialState("ACTIVE", "Activo", "Activo.");
    public static final ComercialState INACTIVE = new ComercialState("INACTIVE", "Inactivo", "Inactivo.");

}
