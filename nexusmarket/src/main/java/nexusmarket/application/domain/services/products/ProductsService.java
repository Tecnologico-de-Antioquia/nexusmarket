package nexusmarket.application.domain.services.products;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.DomainException;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.ports.out.ProductRepositoryPort;

@Service
@RequiredArgsConstructor
public class ProductsService {

    private final ProductRepositoryPort productRepositoryPort;

    public Product createProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product is required");
        }
        return productRepositoryPort.save(product);
    }

    public Product updateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product is required");
        }
        if (productRepositoryPort.findById(product.getId()).isEmpty()) {
            throw new DomainException("Product does not exist");
        }
        return productRepositoryPort.save(product);
    }

    public Product findById(String productId) {
        return productRepositoryPort.findById(productId)
                .orElseThrow(() -> new DomainException("Product was not found"));
    }

    public List<Product> findBySellerId(String sellerId) {
        return productRepositoryPort.findBySellerId(sellerId);
    }

    public List<Product> findAll() {
        return productRepositoryPort.findAll();
    }
}