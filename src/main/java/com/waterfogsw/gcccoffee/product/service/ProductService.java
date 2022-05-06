package com.waterfogsw.gcccoffee.product.service;

import com.waterfogsw.gcccoffee.product.model.Product;

import java.util.List;

public interface ProductService {
    void addProduct(Product product);

    List<Product> findAllProduct();

    Product findById(long id);

    void removeProduct(long id);
}
