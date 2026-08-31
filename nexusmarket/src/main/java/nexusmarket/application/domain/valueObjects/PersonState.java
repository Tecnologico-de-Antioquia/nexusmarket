package nexusmarket.application.domain.valueObjects;

public final class PersonState {
    private final String code;
    private final String name;
    private final String description;


    private PersonState(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public static final PersonState ACTIVE = new PersonState("ACTIVE", "Activo", "Activo.");
    public static final PersonState INACTIVE = new PersonState("INACTIVE", "Inactivo", "Inactivo.");

}
