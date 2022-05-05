package com.waterfogsw.gcccoffee.order.repository;

import com.waterfogsw.gcccoffee.order.model.Order;
import com.waterfogsw.gcccoffee.order.model.OrderProduct;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Map<String, Object> toOrderParamMap(final Order order) {
        final var paramMap = new HashMap<String, Object>();
        paramMap.put("id", order.getId());
        paramMap.put("email", order.getEmail().getAddress());
        paramMap.put("address", order.getAddress());
        paramMap.put("postcode", order.getPostcode());
        paramMap.put("orderStatus", order.getOrderStatus().toString());
        paramMap.put("createdAt", Timestamp.valueOf(order.getCreatedAt()));
        paramMap.put("updatedAt", Timestamp.valueOf(order.getUpdatedAt()));
        return paramMap;
    }

    private Map<String, Object> toOrderProductParamMap(long orderId,
                                                       LocalDateTime createdAt,
                                                       LocalDateTime updatedAt,
                                                       OrderProduct orderProduct) {

        final var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId);
        paramMap.put("productId", orderProduct.id());
        paramMap.put("category", orderProduct.category().name());
        paramMap.put("price", orderProduct.price());
        paramMap.put("quantity", orderProduct.quantity());
        paramMap.put("createdAt", createdAt);
        paramMap.put("updatedAt", updatedAt);
        return paramMap;
    }

    @Override
    public void insert(Order order) {
        if (order == null) {
            throw new IllegalArgumentException();
        }

        final var affectedRows = jdbcTemplate.update("INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at) " +
                "VALUES (:id, :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)", toOrderParamMap(order));

        if (affectedRows != 1) {
            throw new IllegalStateException();
        }

        final var orderId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Collections.emptyMap(), Long.class);

        if (orderId == null) {
            throw new IllegalStateException();
        }

        order.getOrderItems().forEach(item -> jdbcTemplate.update("INSERT INTO order_products(order_id, product_id, category, price, quantity, created_at, updated_at) " +
                        "VALUES (:orderId, :productId, :category, :price, :quantity, :createdAt, :updatedAt)",
                toOrderProductParamMap(orderId, order.getCreatedAt(), order.getUpdatedAt(), item)));
    }
}
