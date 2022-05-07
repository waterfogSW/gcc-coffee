package com.waterfogsw.gcccoffee.product.controller.dto;

import com.waterfogsw.gcccoffee.product.model.Category;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record ProductModifyRequest(
        @NotNull(message = "Name should not be null")
        String name,
        @NotNull(message = "Category should not be null")
        Category category,
        @Positive(message = "Price should be positive value")
        int price,
        String description
) {
}
