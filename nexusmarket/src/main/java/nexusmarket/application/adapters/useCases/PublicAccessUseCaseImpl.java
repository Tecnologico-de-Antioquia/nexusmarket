package nexusmarket.application.adapters.useCases;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.AuthenticationResult;
import nexusmarket.application.domain.models.Buyer;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.in.PublicAccessPort;
import nexusmarket.application.domain.services.buyer.RegisterBuyerService;
import nexusmarket.application.domain.services.user.LoginService;
import nexusmarket.application.domain.services.user.LogoutService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublicAccessUseCaseImpl implements PublicAccessPort {

    private final LoginService loginService;
    private final LogoutService logoutService;
    private final RegisterBuyerService registerBuyerService;

    @Override
    public AuthenticationResult login(User user) {
        return loginService.login(user);
    }

    @Override
    public void logout(User user) {
        logoutService.logout(user);
    }

    @Override
    public Buyer registerBuyer(Buyer buyer) {
        return registerBuyerService.register(buyer);
    }
}
