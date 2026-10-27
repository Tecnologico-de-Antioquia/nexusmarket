package nexusmarket.application.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.ports.out.ProductRepositoryPort;

@Repository
public class InMemoryProductRepository implements ProductRepositoryPort {
    private final Map<String, Product> products = new ConcurrentHashMap<>();

    @Override
    public Product save(Product product) {
        products.put(product.getId(), product);
        return product;
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public List<Product> findBySellerId(String sellerId) {
        return products.values().stream()
                .filter(product -> product.getSellerId().equals(sellerId))
                .toList();
    }

    @Override
    public void delete(String id) {
        products.remove(id);
    }
}
