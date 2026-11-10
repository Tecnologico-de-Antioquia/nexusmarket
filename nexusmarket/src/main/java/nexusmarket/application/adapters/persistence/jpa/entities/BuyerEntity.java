package nexusmarket.application.adapters.persistence.jpa.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "buyers")
@Getter
@Setter
@NoArgsConstructor
public class BuyerEntity {

    @Id
    private String id;

    @Column(unique = true)
    private String userId;

    private String fullName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String password;
    private String role;
    private String state;
    private String commercialState;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "primary_address_id")
    private AddressEntity primaryAddress;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "buyer_id")
    private List<AddressEntity> additionalAddresses = new ArrayList<>();
}
