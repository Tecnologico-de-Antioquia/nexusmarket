package nexusmarket.application.domain.models;

public class Role {
    private String code;
    private String name;
    private String description;

    protected Role(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
