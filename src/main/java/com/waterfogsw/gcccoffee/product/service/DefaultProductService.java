package com.waterfogsw.gcccoffee.product.service;

import com.waterfogsw.gcccoffee.product.model.Product;
import com.waterfogsw.gcccoffee.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;

    public DefaultProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException();
        }

        return productRepository.insertProduct(product);
    }
}
