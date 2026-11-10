package nexusmarket.application.adapters.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    private String id;

    private String fullName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String password;
    private String role;
    private String state;
    private Integer authTokenVersion;
}
