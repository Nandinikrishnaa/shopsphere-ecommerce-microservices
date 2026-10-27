package com.shopsphere.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String sku,
        String name,
        String description,
        String category,
        BigDecimal price,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}