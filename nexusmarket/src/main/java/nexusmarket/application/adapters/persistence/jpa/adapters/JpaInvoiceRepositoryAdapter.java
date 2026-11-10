package nexusmarket.application.adapters.persistence.jpa.adapters;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.persistence.jpa.entities.InvoiceEntity;
import nexusmarket.application.adapters.persistence.jpa.mappers.JpaMapper;
import nexusmarket.application.adapters.persistence.jpa.repositories.JpaInvoiceRepository;
import nexusmarket.application.domain.models.Invoice;
import nexusmarket.application.domain.ports.out.InvoiceRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaInvoiceRepositoryAdapter implements InvoiceRepositoryPort {

    private final JpaInvoiceRepository repository;

    @Override
    public Invoice save(Invoice invoice) {
        InvoiceEntity entity = repository.save(JpaMapper.toEntity(invoice));
        invoice.setId(entity.getId());
        return invoice;
    }

    @Override
    public Optional<Invoice> findById(String id) {
        return repository.findById(id).map(JpaMapper::toDomain);
    }

    @Override
    public Optional<Invoice> findByOrderId(String orderId) {
        return repository.findByOrderId(orderId).map(JpaMapper::toDomain);
    }

    @Override
    public List<Invoice> findAll() {
        return repository.findAll().stream().map(JpaMapper::toDomain).toList();
    }
}
