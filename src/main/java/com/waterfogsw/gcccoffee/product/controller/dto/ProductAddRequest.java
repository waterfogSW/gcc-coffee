package com.waterfogsw.gcccoffee.product.controller.dto;

import com.waterfogsw.gcccoffee.product.model.Category;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record ProductAddRequest(
        @NotNull(message = "Product name should not be null")
        String name,

        @NotNull(message = "Product category should not be null")
        Category category,

        @Positive(message = "Product price should be positive")
        int price,

        String description
) {
}
