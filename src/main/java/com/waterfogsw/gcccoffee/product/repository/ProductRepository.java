package com.waterfogsw.gcccoffee.product.repository;

import com.waterfogsw.gcccoffee.product.model.Product;

import java.util.List;

public interface ProductRepository {
    Product insert(Product product);

    List<Product> selectAll();
}
