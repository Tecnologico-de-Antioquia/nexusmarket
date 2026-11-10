package nexusmarket.application.adapters.persistence.jpa.repositories;

import nexusmarket.application.adapters.persistence.jpa.entities.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaWarehouseRepository extends JpaRepository<WarehouseEntity, String> {

    List<WarehouseEntity> findByOwnerId(String ownerId);
}
