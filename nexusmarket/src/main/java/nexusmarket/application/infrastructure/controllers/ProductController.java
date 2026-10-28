package nexusmarket.application.infrastructure.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.Digital;
import nexusmarket.application.domain.models.Physical;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.models.Variant;
import nexusmarket.application.domain.services.products.ProductsService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductsService productsService;

    @GetMapping("/products")
    public List<Product> findAll() {
        return productsService.findAll();
    }

    @GetMapping("/products/{id}")
    public Product findById(@PathVariable String id) {
        return productsService.findById(id);
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody ProductRequest request) {
        Product product;

        if ("PHYSICAL".equalsIgnoreCase(request.type())) {
            product = new Physical(
                    request.id(),
                    request.name(),
                    request.description(),
                    request.sellerId(),
                    request.stock(),
                    new Variant(request.color(), request.size(), request.model()));
        } else {
            product = new Digital(
                    request.id(),
                    request.name(),
                    request.description(),
                    request.sellerId(),
                    request.format(),
                    request.downloadUrl());
        }

        return productsService.createProduct(product);
    }

    public record ProductRequest(
            String id,
            String name,
            String description,
            String sellerId,
            String type,
            Integer stock,
            String color,
            Integer size,
            String model,
            String format,
            String downloadUrl) {}
}
