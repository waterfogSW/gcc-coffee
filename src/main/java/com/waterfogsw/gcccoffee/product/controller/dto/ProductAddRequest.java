package com.waterfogsw.gcccoffee.product.controller.dto;

import com.waterfogsw.gcccoffee.product.model.Category;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record ProductAddRequest(
        @NotNull
        String name,
        @NotNull
        Category category,
        @Positive
        int price,
        String description
) {
}
