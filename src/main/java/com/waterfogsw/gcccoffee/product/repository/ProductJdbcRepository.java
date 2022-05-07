package com.waterfogsw.gcccoffee.product.repository;

import com.waterfogsw.gcccoffee.common.exception.ResourceNotFound;
import com.waterfogsw.gcccoffee.product.model.Category;
import com.waterfogsw.gcccoffee.product.model.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class ProductJdbcRepository implements ProductRepository {

    private static final RowMapper<Product> productRowMapper = (resultSet, i) -> {
        final var id = resultSet.getLong("product_id");
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
    @Transactional
    public void insert(Product product) {
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
            return;
        }

        final var updateSql = "UPDATE products SET name = :name, category = :category, price = :price, " +
                "description = :description, updated_at = :updatedAt WHERE product_id = :id";
        final var affectedRows = jdbcTemplate.update(updateSql, toParamMap(product));

        if (affectedRows != 1) {
            throw new IllegalStateException("Error occur while update");
        }
    }

    @Override
    @Transactional
    public List<Product> selectAll() {
        return jdbcTemplate.query("select * from products", productRowMapper);
    }

    @Override
    @Transactional
    public Optional<Product> selectById(long id) {
        final var param = Collections.singletonMap("id", id);
        return jdbcTemplate.query("select * from products where product_id = :id", param, productRowMapper)
                .stream().findAny();
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        final var deleteSql = "delete from products where product_id = :id";
        final var affectedRow = jdbcTemplate.update(deleteSql, Collections.singletonMap("id", String.valueOf(id)));

        if (affectedRow != 1) {
            throw new ResourceNotFound();
        }
    }

}
