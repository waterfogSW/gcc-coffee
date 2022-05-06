package com.waterfogsw.gcccoffee.product.repository;

import com.waterfogsw.gcccoffee.product.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void insert(Product product);

    List<Product> selectAll();

    Optional<Product> selectById(long id);

    void deleteById(long id);
}
