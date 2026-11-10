package nexusmarket.application.adapters.persistence.jpa.entities;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "warehouses")
@Getter
@Setter
@NoArgsConstructor
public class WarehouseEntity {

    @Id
    private String id;
    private String name;

    @Embedded
    private AddressEmbeddable address;

    private Integer capacity;
    private String type;
    private String ownerId;
}
