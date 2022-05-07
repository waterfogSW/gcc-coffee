package com.waterfogsw.gcccoffee.order.controller.api;

import com.waterfogsw.gcccoffee.order.controller.dto.OrderAddRequest;
import com.waterfogsw.gcccoffee.order.model.Order;
import com.waterfogsw.gcccoffee.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/v1/orders")
@RestController
public class OrderApiController {
    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void orderAdd(@Valid @RequestBody OrderAddRequest request) {
        orderService.addOrder(Order.from(request));
    }
}
