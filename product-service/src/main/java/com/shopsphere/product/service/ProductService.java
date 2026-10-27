package com.shopsphere.product.service;

import com.shopsphere.product.dto.CreateProductRequest;
import com.shopsphere.product.dto.ProductResponse;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest request);

    ProductResponse getById(Long id);

    Page<ProductResponse> getAll(Pageable pageable);

    ProductResponse updateProduct(Long id, CreateProductRequest request);

    void deactivateProduct(Long id);
}
