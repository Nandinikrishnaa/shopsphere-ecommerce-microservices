package com.shopsphere.product.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateProductRequest(

        @NotBlank
        @Size(max = 50)
        String sku,

        @NotBlank
        @Size(max = 150)
        String name,

        @Size(max = 1000)
        String description,

        @NotBlank
        @Size(max = 100)
        String category,

        @NotNull
        @DecimalMin(value = "0.00")
        @Digits(integer = 10, fraction = 2)
        BigDecimal price
) {}