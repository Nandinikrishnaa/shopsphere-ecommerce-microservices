package com.shopsphere.product.service.implementation;

import com.shopsphere.product.dto.CreateProductRequest;
import com.shopsphere.product.dto.ProductResponse;
import com.shopsphere.product.entity.Products;
import com.shopsphere.product.exception.ResourceNotFoundException;
import com.shopsphere.product.repository.ProductRepository;
import com.shopsphere.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        String sku = request.sku().trim();

        if (productRepository.existsBySkuIgnoreCase(sku)) {
            throw new IllegalArgumentException(
                    "Product SKU already exists: " + sku);
        }

        Products product = new Products();
        product.setSku(sku);
        product.setName(request.name().trim());
        product.setDescription(request.description());
        product.setCategory(request.category().trim());
        product.setPrice(request.price());

        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        Products product = productRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Resource not found "+id));

        return toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAll(Pageable pageable) {

        return productRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, CreateProductRequest request) {
        Products product = productRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("Product not found: " + id));

        String requestedSku = request.sku().trim();

        if (!product.getSku().equalsIgnoreCase(requestedSku)
                && productRepository.existsBySkuIgnoreCase(requestedSku)) {
            throw new IllegalArgumentException("Product SKU already exists: " + requestedSku);
        }

        product.setSku(requestedSku);
        product.setName(request.name().trim());
        product.setDescription(request.description());
        product.setCategory(request.category().trim());
        product.setPrice(request.price());

        return toResponse(productRepository.save(product));

    }

    @Override
    public void deactivateProduct(Long id) {
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));

        product.setActive(false);
        productRepository.save(product);

    }

    private ProductResponse toResponse(Products product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getPrice(),
                product.isActive(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
