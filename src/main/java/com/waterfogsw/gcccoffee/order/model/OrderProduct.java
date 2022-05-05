package com.waterfogsw.gcccoffee.order.model;

import com.waterfogsw.gcccoffee.product.model.Category;

public record OrderProduct(
        long id,
        Category category,
        int price,
        int quantity
) {
}
