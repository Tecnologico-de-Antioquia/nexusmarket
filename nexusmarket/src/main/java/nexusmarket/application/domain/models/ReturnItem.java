package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReturnItem {

    private String id;
    private OrderItem orderItem;
    private Integer quantity;
}
