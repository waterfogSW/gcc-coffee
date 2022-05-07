package com.waterfogsw.gcccoffee.order.model;

import com.waterfogsw.gcccoffee.order.controller.dto.OrderAddRequest;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Order {
    @Min(value = 0, message = "Order id should be at least zero")
    private final long id;

    @Email(message = "Invalid email format")
    private final String email;

    @NotNull(message = "Address should not be null")
    private String address;

    @NotNull(message = "Postcode should not be null")
    private String postcode;

    @NotEmpty(message = "Order Products should not be empty")
    private final List<OrderProduct> orderProducts;

    private OrderStatus orderStatus;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(long id, String email, String address, String postcode, List<OrderProduct> orderProducts, OrderStatus orderStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.orderProducts = orderProducts;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Order(String email, String address, String postcode, List<OrderProduct> orderProducts) {
        this(0, email, address, postcode, orderProducts, OrderStatus.ACCEPTED, LocalDateTime.now(), LocalDateTime.now());
    }

    public static Order from(OrderAddRequest request) {
        return new Order(
                request.email(),
                request.address(),
                request.postcode(),
                request.orderProducts()
                        .stream()
                        .map(OrderProduct::from)
                        .collect(Collectors.toList())
        );
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
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
