package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Variant {

    private String color;
    private Integer size;
    private String model;
}
