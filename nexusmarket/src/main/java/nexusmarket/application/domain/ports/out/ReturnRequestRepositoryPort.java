package nexusmarket.application.domain.ports.out;

import nexusmarket.application.domain.models.ReturnRequest;

import java.util.List;
import java.util.Optional;

public interface ReturnRequestRepositoryPort {

    ReturnRequest save(ReturnRequest returnRequest);

    Optional<ReturnRequest> findById(String id);

    Optional<ReturnRequest> findByOrderId(String orderId);

    List<ReturnRequest> findAll();

    List<ReturnRequest> findByBuyerId(String buyerId);
}
