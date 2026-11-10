package nexusmarket.application.domain.ports.out;

import nexusmarket.application.domain.models.Warehouse;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepositoryPort {

    Warehouse save(Warehouse warehouse);

    Optional<Warehouse> findById(String id);

    List<Warehouse> findAll();

    List<Warehouse> findBySellerId(String sellerId);

    void delete(String id);
}
