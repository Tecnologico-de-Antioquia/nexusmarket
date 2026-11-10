package nexusmarket.application.adapters.persistence.jpa.entities;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@NoArgsConstructor
public class ShipmentEntity {

    @Id
    private String id;

    @Column(unique = true)
    private String orderId;

    private String trackingGuide;
    private String state;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "origin_street")),
            @AttributeOverride(name = "city", column = @Column(name = "origin_city")),
            @AttributeOverride(name = "state", column = @Column(name = "origin_state")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "origin_postal_code")),
            @AttributeOverride(name = "country", column = @Column(name = "origin_country")),
            @AttributeOverride(name = "additionalInfo", column = @Column(name = "origin_additional_info"))
    })
    private AddressEmbeddable originAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "destination_street")),
            @AttributeOverride(name = "city", column = @Column(name = "destination_city")),
            @AttributeOverride(name = "state", column = @Column(name = "destination_state")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "destination_postal_code")),
            @AttributeOverride(name = "country", column = @Column(name = "destination_country")),
            @AttributeOverride(name = "additionalInfo", column = @Column(name = "destination_additional_info"))
    })
    private AddressEmbeddable destinationAddress;
}
