package com.waterfogsw.gcccoffee.product.repository;

import com.waterfogsw.gcccoffee.product.model.Category;
import com.waterfogsw.gcccoffee.product.model.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ProductJdbcRepository implements ProductRepository {

    private static final RowMapper<Product> productRowMapper = (resultSet, i) -> {
        final var id = resultSet.getLong("id");
        final var name = resultSet.getString("name");
        final var category = Category.valueOf(resultSet.getString("category"));
        final var price = resultSet.getInt("price");
        final var description = resultSet.getString("description");
        final var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        final var updatedAt = resultSet.getTimestamp("updated_at").toLocalDateTime();
        return new Product(id, name, category, price, description, createdAt, updatedAt);
    };

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Map<String, Object> toParamMap(Product product) {
        return new HashMap<>() {{
            put("id", product.getId());
            put("name", product.getName());
            put("category", product.getCategory().name());
            put("price", product.getPrice());
            put("description", product.getDescription());
            put("createdAt", Timestamp.valueOf(product.getCreatedAt()));
            put("updatedAt", Timestamp.valueOf(product.getUpdatedAt()));
        }};
    }

    @Override
    public Product insert(Product product) {
        if (product == null) {
            throw new IllegalArgumentException();
        }

        if (product.getId() == 0) {
            final var insertSql = "INSERT INTO products(name, category, price, description, created_at, updated_at) " +
                    "VALUES (:name, :category, :price, :description, :createdAt, :updatedAt)";

            final var affectedRows = jdbcTemplate.update(insertSql, toParamMap(product));
            if (affectedRows != 1) {
                throw new IllegalStateException("Nothing was inserted");
            }

            final var id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Collections.emptyMap(), Long.class);
            if (id == null) {
                throw new IllegalStateException();
            }
            return Product.of(id, product);
        }

        final var updateSql = "UPDATE products SET name = :name, category = :category, price = :price, " +
                "description = :description, updated_at = :updatedAt WHERE id = :id";
        final var affectedRows = jdbcTemplate.update(updateSql, toParamMap(product));

        if (affectedRows != 1) {
            throw new IllegalStateException("Error occur while update");
        }
        return product;
    }
}