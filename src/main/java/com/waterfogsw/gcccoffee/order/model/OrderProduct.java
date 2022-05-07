package com.waterfogsw.gcccoffee.order.model;

import com.waterfogsw.gcccoffee.order.controller.dto.OrderProductAddRequest;
import com.waterfogsw.gcccoffee.product.model.Category;

import javax.validation.constraints.Positive;

public class OrderProduct {
    @Positive
    private final long id;

    private final Category category;

    @Positive
    private final int price;

    @Positive
    private final int quantity;

    public long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderProduct(long id, Category category, int price, int quantity) {
        this.id = id;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderProduct from(OrderProductAddRequest orderProductAddRequest) {
        return new OrderProduct(orderProductAddRequest.id(),
                orderProductAddRequest.category(),
                orderProductAddRequest.price(),
                orderProductAddRequest.quantity());
    }
}
