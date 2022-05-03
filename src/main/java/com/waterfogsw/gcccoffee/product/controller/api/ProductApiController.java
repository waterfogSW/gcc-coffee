package com.waterfogsw.gcccoffee.product.controller.api;

import com.waterfogsw.gcccoffee.product.controller.dto.ProductAddRequest;
import com.waterfogsw.gcccoffee.product.controller.dto.ProductResponse;
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
    public ProductResponse productAdd(@RequestBody ProductAddRequest request) {
        final var addedProduct = productService.addProduct(Product.from(request));
        return ProductResponse.from(addedProduct);
    }

    @GetMapping
    public List<ProductResponse> productList(SortType type) {
        return productService.findAllProduct()
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

}
