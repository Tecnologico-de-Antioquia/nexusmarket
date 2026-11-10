package nexusmarket.application.domain.ports.in;

import nexusmarket.application.domain.models.AuthenticationResult;
import nexusmarket.application.domain.models.Buyer;
import nexusmarket.application.domain.models.User;

public interface PublicAccessPort {

    AuthenticationResult login(User user);

    void logout(User user);

    Buyer registerBuyer(Buyer buyer);
}
