package nexusmarket.application.adapters.persistence.jpa.adapters;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.persistence.jpa.entities.RefundEntity;
import nexusmarket.application.adapters.persistence.jpa.mappers.JpaMapper;
import nexusmarket.application.adapters.persistence.jpa.repositories.JpaRefundRepository;
import nexusmarket.application.domain.models.Refund;
import nexusmarket.application.domain.ports.out.RefundRepositoryPort;
import nexusmarket.application.domain.ports.out.ReturnRequestRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaRefundRepositoryAdapter implements RefundRepositoryPort {

    private final JpaRefundRepository repository;
    private final ReturnRequestRepositoryPort returnRequestRepositoryPort;

    @Override
    public Refund save(Refund refund) {
        RefundEntity entity = repository.save(JpaMapper.toEntity(refund));
        refund.setId(entity.getId());
        return refund;
    }

    @Override
    public Optional<Refund> findById(String id) {
        return repository.findById(id).map(this::enrich);
    }

    @Override
    public Optional<Refund> findByReturnId(String returnId) {
        return repository.findByReturnId(returnId).map(this::enrich);
    }

    @Override
    public List<Refund> findAll() {
        return repository.findAll().stream().map(this::enrich).toList();
    }

    @Override
    public List<Refund> findByBuyerId(String buyerId) {
        return repository.findAll().stream()
                .map(this::enrich)
                .filter(refund -> refund.getReturnRequest() != null
                        && refund.getReturnRequest().getOrder() != null
                        && refund.getReturnRequest().getOrder().getBuyer() != null
                        && buyerId.equals(refund.getReturnRequest().getOrder().getBuyer().getId()))
                .toList();
    }

    private Refund enrich(RefundEntity entity) {
        Refund refund = JpaMapper.toDomain(entity);
        if (refund != null && entity.getReturnId() != null) {
            returnRequestRepositoryPort.findById(entity.getReturnId()).ifPresent(refund::setReturnRequest);
        }
        return refund;
    }
}
