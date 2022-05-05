package com.waterfogsw.gcccoffee.order.model;

import com.waterfogsw.gcccoffee.order.controller.dto.OrderAddRequest;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private final long id;
    private final Email email;
    private String address;
    private String postcode;
    private final List<OrderProduct> orderProducts;
    private OrderStatus orderStatus;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(long id, Email email, String address, String postcode, List<OrderProduct> orderProducts, OrderStatus orderStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.orderProducts = orderProducts;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Order(Email email, String address, String postcode, List<OrderProduct> orderProducts) {
        this(0, email, address, postcode, orderProducts, OrderStatus.ACCEPTED, LocalDateTime.now(), LocalDateTime.now());
    }

    public static Order from(OrderAddRequest request) {
        return new Order(new Email(request.email()), request.address(), request.postcode(), request.orderProducts());
    }

    public static Order of(long id, Order order) {
        return new Order(id, order.getEmail(), order.getAddress(), order.getPostcode(), order.getOrderItems(), order.getOrderStatus(), order.getCreatedAt(), order.getUpdatedAt());
    }


    public void setAddress(String address) {
        this.address = address;
        this.updatedAt = LocalDateTime.now();
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
        this.updatedAt = LocalDateTime.now();
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPostcode() {
        return postcode;
    }

    public List<OrderProduct> getOrderItems() {
        return orderProducts;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
