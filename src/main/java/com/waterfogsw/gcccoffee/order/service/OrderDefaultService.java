package com.waterfogsw.gcccoffee.order.service;

import com.waterfogsw.gcccoffee.order.model.Order;
import com.waterfogsw.gcccoffee.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderDefaultService implements OrderService {

    private final OrderRepository orderRepository;

    public OrderDefaultService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order addOrder(Order order) {
        if(order == null) {
            throw new IllegalArgumentException();
        }

        return orderRepository.insert(order);
    }
}
