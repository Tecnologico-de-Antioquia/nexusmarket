package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Variant {
    private String color;
    private Integer size;
    private String model;

    public Variant(String color, Integer size, String model) {
        this.color = color;
        this.size = size;
        this.model = model;
    }
}
