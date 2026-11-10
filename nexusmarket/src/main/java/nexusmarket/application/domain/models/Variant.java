package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.exceptions.InvalidVariantException;

@Getter
@Setter
@NoArgsConstructor
public class Variant {

    private String color;
    private Integer size;
    private String model;

    public void ensureValid() {
        boolean empty = (color == null || color.isBlank())
                && size == null
                && (model == null || model.isBlank());
        if (empty) {
            throw new InvalidVariantException("La variante debe definir al menos un atributo.");
        }
    }
}
