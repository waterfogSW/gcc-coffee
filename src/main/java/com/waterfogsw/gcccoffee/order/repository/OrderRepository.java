package com.waterfogsw.gcccoffee.order.repository;

import com.waterfogsw.gcccoffee.order.model.Order;

public interface OrderRepository {
    Order insert(Order order);
}
