package com.waterfogsw.gcccoffee.product.controller.dto;

import com.waterfogsw.gcccoffee.product.model.Category;
import com.waterfogsw.gcccoffee.product.model.Product;

import java.time.LocalDateTime;

public record ProductResponse(
        long id,
        String name,
        Category category,
        long price,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getDescription(),
                product.getCreatedAt(),
                product.getUpdatedAt());
    }
}
