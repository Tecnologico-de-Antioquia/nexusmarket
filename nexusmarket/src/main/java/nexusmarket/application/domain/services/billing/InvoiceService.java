package nexusmarket.application.domain.services.billing;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.UnauthorizedOperationException;
import nexusmarket.application.domain.models.Invoice;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.InvoiceRepositoryPort;
import nexusmarket.application.domain.ports.out.OrderRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepositoryPort invoiceRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final AuthorizationService authorizationService;

    public List<Invoice> consultMyInvoices(User user) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        return orderRepositoryPort.findByBuyerId(user.getId()).stream()
                .map(Order::getInvoice)
                .filter(Objects::nonNull)
                .toList();
    }

    public Invoice consultMyInvoice(User user, String invoiceId) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        Invoice invoice = invoiceRepositoryPort.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada."));
        if (invoice.getOrder() == null || invoice.getOrder().getBuyer() == null
                || !invoice.getOrder().getBuyer().getId().equals(user.getId())) {
            throw new UnauthorizedOperationException("La factura no pertenece al comprador.");
        }
        return invoice;
    }

    public List<Invoice> consultAll(User user) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.SUPERVISOR);
        return invoiceRepositoryPort.findAll();
    }

    public Invoice consultById(User user, String invoiceId) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.SUPERVISOR);
        return invoiceRepositoryPort.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada."));
    }
}
