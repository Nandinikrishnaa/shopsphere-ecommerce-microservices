package com.shopsphere.product.repository;

import com.shopsphere.product.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
    boolean existsBySkuIgnoreCase(String sku);

    Optional<Products> findByIdAndActiveTrue(Long id);

}
