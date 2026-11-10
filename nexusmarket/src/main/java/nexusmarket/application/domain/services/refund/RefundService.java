package nexusmarket.application.domain.services.refund;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.DomainException;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.InvalidStateTransitionException;
import nexusmarket.application.domain.models.Refund;
import nexusmarket.application.domain.models.ReturnRequest;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.PaymentGatewayPort;
import nexusmarket.application.domain.ports.out.RefundRepositoryPort;
import nexusmarket.application.domain.ports.out.ReturnRequestRepositoryPort;
import nexusmarket.application.domain.services.operation.RegisterAuditLogService;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.ReturnState;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepositoryPort refundRepositoryPort;
    private final ReturnRequestRepositoryPort returnRequestRepositoryPort;
    private final PaymentGatewayPort paymentGatewayPort;
    private final RegisterAuditLogService registerAuditLogService;
    private final AuthorizationService authorizationService;

    public Refund process(User admin, String refundId) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        Refund refund = refundRepositoryPort.findById(refundId)
                .orElseThrow(() -> new EntityNotFoundException("Reembolso no encontrado."));
        ReturnRequest returnRequest = refund.getReturnRequest();
        if (returnRequest == null || !ReturnState.RECEIVED.equals(returnRequest.getState())) {
            throw new InvalidStateTransitionException("La devolucion debe estar recibida para reembolsar.");
        }
        boolean success = paymentGatewayPort.refund(returnRequest.getOrder().getId(), refund.getAmount());
        if (!success) {
            refund.fail();
            refundRepositoryPort.save(refund);
            throw new DomainException("El reembolso fue rechazado por la pasarela.");
        }
        refund.process();
        Refund saved = refundRepositoryPort.save(refund);
        returnRequest.complete();
        returnRequestRepositoryPort.save(returnRequest);
        registerAuditLogService.register(admin, "REFUND_PROCESSED", "REFUND", saved.getId(),
                Map.of("amount", saved.getAmount()));
        return saved;
    }

    public List<Refund> consultMyRefunds(User user) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        return refundRepositoryPort.findAll().stream()
                .filter(refund -> refund.getReturnRequest() != null
                        && refund.getReturnRequest().getOrder() != null
                        && refund.getReturnRequest().getOrder().getBuyer() != null
                        && user.getId().equals(refund.getReturnRequest().getOrder().getBuyer().getId()))
                .toList();
    }

    public List<Refund> consultAll(User user) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.SUPERVISOR);
        return refundRepositoryPort.findAll();
    }
}
