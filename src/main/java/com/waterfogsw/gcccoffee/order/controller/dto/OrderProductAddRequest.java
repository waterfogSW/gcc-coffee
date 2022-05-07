package com.waterfogsw.gcccoffee.order.controller.dto;

import com.waterfogsw.gcccoffee.product.model.Category;

import javax.validation.constraints.Positive;

public record OrderProductAddRequest(
        @Positive(message = "Product id should be positive")
        long id,

        Category category,

        @Positive(message = "Product price should be positive")
        int price,

        @Positive(message = "product quantity should be positive")
        int quantity
) {
}
