package nexusmarket.application.adapters.persistence.jpa.adapters;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.persistence.jpa.entities.ProductEntity;
import nexusmarket.application.adapters.persistence.jpa.mappers.JpaMapper;
import nexusmarket.application.adapters.persistence.jpa.repositories.JpaProductRepository;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.ports.out.ProductRepositoryPort;
import nexusmarket.application.domain.ports.out.SellerRepositoryPort;
import nexusmarket.application.domain.valueobjects.ProductState;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaProductRepositoryAdapter implements ProductRepositoryPort {

    private final JpaProductRepository repository;
    private final SellerRepositoryPort sellerRepositoryPort;

    @Override
    public Product save(Product product) {
        ProductEntity entity = repository.save(JpaMapper.toEntity(product));
        product.setId(entity.getId());
        return product;
    }

    @Override
    public Optional<Product> findById(String id) {
        return repository.findById(id).map(this::enrich);
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll().stream().map(this::enrich).toList();
    }

    @Override
    public List<Product> findBySellerId(String sellerId) {
        return repository.findBySellerId(sellerId).stream().map(this::enrich).toList();
    }

    @Override
    public List<Product> findPublished() {
        return repository.findByState(ProductState.PUBLISHED.getCode()).stream().map(this::enrich).toList();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    private Product enrich(ProductEntity entity) {
        Product product = JpaMapper.toDomain(entity);
        if (product != null && product.getSeller() != null) {
            sellerRepositoryPort.findById(product.getSeller().getId()).ifPresent(product::setSeller);
        }
        return product;
    }
}
