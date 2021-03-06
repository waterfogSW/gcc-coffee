package com.waterfogsw.gcccoffee.product.service;

import com.waterfogsw.gcccoffee.common.exception.ResourceNotFound;
import com.waterfogsw.gcccoffee.product.model.Product;
import com.waterfogsw.gcccoffee.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDefaultService implements ProductService {

    private final ProductRepository productRepository;

    public ProductDefaultService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void saveProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException();
        }

        productRepository.insert(product);
    }

    @Override
    public List<Product> findAllProduct() {
        return productRepository.selectAll();
    }

    @Override
    public Product findById(long id) {
        final var findProduct = productRepository.selectById(id);
        if (findProduct.isEmpty()) {
            throw new ResourceNotFound();
        }
        return findProduct.get();
    }

    @Override
    public void removeProduct(long id) {
        productRepository.deleteById(id);
    }
}
