package nexusmarket.application.domain.ports.out;

import nexusmarket.application.domain.models.Refund;

import java.util.List;
import java.util.Optional;

public interface RefundRepositoryPort {

    Refund save(Refund refund);

    Optional<Refund> findById(String id);

    Optional<Refund> findByReturnId(String returnId);

    List<Refund> findAll();

    List<Refund> findByBuyerId(String buyerId);
}
