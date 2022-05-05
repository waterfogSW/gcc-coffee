package com.waterfogsw.gcccoffee.product.controller.api;

import com.waterfogsw.gcccoffee.product.controller.dto.ProductAddRequest;
import com.waterfogsw.gcccoffee.product.controller.dto.ProductResponse;
import com.waterfogsw.gcccoffee.product.model.Category;
import com.waterfogsw.gcccoffee.product.model.Product;
import com.waterfogsw.gcccoffee.product.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductApiController {
    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public void productAdd(@RequestBody ProductAddRequest request) {
        productService.addProduct(Product.from(request));
    }

    @GetMapping
    public List<ProductResponse> productList(SortType sort, Category category) {
        return productService.findAllProduct()
                .stream()
                .filter(product -> category == null || product.getCategory() == category)
                .sorted(sort == null ? Comparator.comparing(Product::getId) : sort.getComparator())
                .map(ProductResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductResponse productDetail(@PathVariable("id") long id) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }

        final var product = productService.findById(id);
        return ProductResponse.from(product);
    }
}
