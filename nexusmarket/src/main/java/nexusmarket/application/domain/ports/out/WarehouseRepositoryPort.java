package nexusmarket.application.domain.ports.out;

import java.util.List;
import java.util.Optional;

import nexusmarket.application.domain.models.Warehouse;

public interface WarehouseRepositoryPort {
    Warehouse save(Warehouse warehouse);
    Optional<Warehouse> findById(String id);
    List<Warehouse> findAll();
    void delete(String id);
}
