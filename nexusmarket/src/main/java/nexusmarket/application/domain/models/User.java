package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.enums.ApprovalDecision;
import nexusmarket.application.domain.exceptions.DomainException;
import nexusmarket.application.domain.exceptions.InvalidApprovalDecisionException;
import nexusmarket.application.domain.valueobjects.Currency;
import nexusmarket.application.domain.valueobjects.Money;
import nexusmarket.application.domain.valueobjects.OrderState;
import nexusmarket.application.domain.valueobjects.PaymentState;
import nexusmarket.application.domain.valueobjects.RefundState;
import nexusmarket.application.domain.valueobjects.ReturnState;
import nexusmarket.application.domain.valueobjects.SystemRole;
import nexusmarket.application.domain.valueobjects.UserStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class User extends Person {

    private Integer authTokenVersion = 1;

    private String username;
    private String password;
    private UserStatus state = UserStatus.ACTIVE;
    private SystemRole role;

    private Person profile; // Buyer or Seller profile; set by registration

    public void bumpAuthTokenVersion() {
        int current = authTokenVersion == null ? 0 : authTokenVersion;
        this.authTokenVersion = current + 1;
    }

    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.state);
    }

    // Profile helpers
    public void setProfile(Person profile) {
        this.profile = profile;
        if (profile instanceof nexusmarket.application.domain.models.Buyer) {
            // ensure role is BUYER
            this.role = SystemRole.BUYER;
        } else if (profile instanceof nexusmarket.application.domain.models.Seller) {
            this.role = SystemRole.SELLER;
        }
    }

    public Person getProfile() {
        return profile;
    }

    // Authentication methods
    public AuthenticationResult authenticate(String token) {
        // Simple wrapper; actual token validation done by JwtProvider
        return null;
    }

    // Money helpers (additive from Phase 1)
    public Money priceAsMoney(Currency currency) {
        return Money.of(BigDecimal.ZERO, currency); // placeholder
    }

    public Money totalAsMoney(Currency currency) {
        return Money.of(BigDecimal.ZERO, currency);
    }
}