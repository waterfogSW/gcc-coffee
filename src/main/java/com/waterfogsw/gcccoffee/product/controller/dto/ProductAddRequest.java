package com.waterfogsw.gcccoffee.product.controller.dto;

import com.waterfogsw.gcccoffee.product.model.Category;

public record ProductAddRequest(
        String name,
        Category category,
        long price,
        String description
) {
    public ProductAddRequest {
        validate(name, category, price);
    }

    private static void validate(String name, Category category, long price) {
        if (name == null || category == null || price <= 0) {
            throw new IllegalArgumentException();
        }
    }
}
