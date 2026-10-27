package nexusmarket.application.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import nexusmarket.application.domain.models.Warehouse;
import nexusmarket.application.domain.ports.out.WarehouseRepositoryPort;

@Repository
public class InMemoryWarehouseRepository implements WarehouseRepositoryPort {
    private final Map<String, Warehouse> warehouses = new ConcurrentHashMap<>();

    @Override
    public Warehouse save(Warehouse warehouse) {
        warehouses.put(warehouse.getId(), warehouse);
        return warehouse;
    }

    @Override
    public Optional<Warehouse> findById(String id) {
        return Optional.ofNullable(warehouses.get(id));
    }

    @Override
    public List<Warehouse> findAll() {
        return new ArrayList<>(warehouses.values());
    }

    @Override
    public void delete(String id) {
        warehouses.remove(id);
    }
}
