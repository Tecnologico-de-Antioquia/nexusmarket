package nexusmarket.application.domain.ports.out;

import java.util.List;
import java.util.Optional;

import nexusmarket.application.domain.models.Product;

public interface ProductRepositoryPort {
    Product save(Product product);
    Optional<Product> findById(String id);
    List<Product> findAll();
    List<Product> findBySellerId(String sellerId);
    void delete(String id);
}
