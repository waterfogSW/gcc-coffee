package com.waterfogsw.gcccoffee.product.repository;

import com.waterfogsw.gcccoffee.product.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public class ProductMemoryRepository implements ProductRepository {
    @Override
    public Product insertProduct(Product product) {
        return null;
    }
}
