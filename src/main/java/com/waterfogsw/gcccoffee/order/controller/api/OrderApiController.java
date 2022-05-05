package com.waterfogsw.gcccoffee.order.controller.api;

import com.waterfogsw.gcccoffee.order.controller.dto.OrderAddRequest;
import com.waterfogsw.gcccoffee.order.model.Order;
import com.waterfogsw.gcccoffee.order.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/order")
@RestController
public class OrderApiController {
    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order orderAdd(final @RequestBody OrderAddRequest request) {
        return orderService.addOrder(Order.from(request));
    }
}