package com.waterfogsw.gcccoffee.order.repository;

import com.waterfogsw.gcccoffee.order.model.Order;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Map<String, Object> toParamMap(Order order) {
        return new HashMap<>() {{
            put("id", order.getId());
            put("email", order.getEmail().getAddress());
            put("address", order.getAddress());
            put("postcode", order.getPostcode());
            put("orderStatus", order.getOrderStatus().toString());
            put("createdAt", Timestamp.valueOf(order.getCreatedAt()));
            put("updatedAt", Timestamp.valueOf(order.getUpdatedAt()));
        }};
    }

    @Override
    public Order insert(Order order) {
        //todo : insert 구현
        return null;
    }
}
