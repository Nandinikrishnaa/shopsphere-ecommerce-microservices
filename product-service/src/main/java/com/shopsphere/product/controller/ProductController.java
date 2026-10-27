package com.shopsphere.product.controller;

import com.shopsphere.product.dto.CreateProductRequest;
import com.shopsphere.product.dto.ProductResponse;
import com.shopsphere.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        return new ResponseEntity<>(productService.createProduct(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id){
        return productService.getById(id);
    }

    @GetMapping()
    public Page<ProductResponse> getAll(@PageableDefault(size=10, sort="id") Pageable pageable){
        return productService.getAll(pageable);
    }

    @PutMapping("/products/{id}")
    public ProductResponse updateProduct(@PathVariable  Long id, @RequestBody CreateProductRequest request){
        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        productService.deactivateProduct(id);
    }

}
