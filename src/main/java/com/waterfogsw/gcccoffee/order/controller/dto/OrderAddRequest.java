package com.waterfogsw.gcccoffee.order.controller.dto;

import com.waterfogsw.gcccoffee.order.model.OrderProduct;

import java.util.List;

public record OrderAddRequest(
        String email,
        String address,
        String postcode,
        List<OrderProduct> orderProducts
) {
    public OrderAddRequest {
        if (email == null || address == null || postcode == null || orderProducts == null) {
            throw new IllegalArgumentException();
        }
    }
}
