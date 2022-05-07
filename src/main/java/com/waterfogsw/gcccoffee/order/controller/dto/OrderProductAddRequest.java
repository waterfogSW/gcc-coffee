package com.waterfogsw.gcccoffee.order.controller.dto;

import com.waterfogsw.gcccoffee.product.model.Category;

import javax.validation.constraints.Positive;

public record OrderProductAddRequest(
        @Positive
        long id,
        Category category,
        @Positive
        int price,
        @Positive
        int quantity
) {
}
