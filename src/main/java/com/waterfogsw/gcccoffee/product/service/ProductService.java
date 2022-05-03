package com.waterfogsw.gcccoffee.product.service;

import com.waterfogsw.gcccoffee.product.model.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(Product product);

    List<Product> findAllProduct();

    Product findById(long id);
}
