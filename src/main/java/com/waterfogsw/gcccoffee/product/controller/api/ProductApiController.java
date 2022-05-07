package com.waterfogsw.gcccoffee.product.controller.api;

import com.waterfogsw.gcccoffee.product.controller.dto.ProductAddRequest;
import com.waterfogsw.gcccoffee.product.controller.dto.ProductModifyRequest;
import com.waterfogsw.gcccoffee.product.controller.dto.ProductResponse;
import com.waterfogsw.gcccoffee.product.model.Category;
import com.waterfogsw.gcccoffee.product.model.Product;
import com.waterfogsw.gcccoffee.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @ResponseStatus(HttpStatus.CREATED)
    public void productAdd(@Valid @RequestBody ProductAddRequest request) {
        productService.saveProduct(Product.from(request));
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

    @PutMapping("/{id}")
    public void productModify(@PathVariable("id") long id, @Valid @RequestBody ProductModifyRequest request) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }

        final var targetProduct = productService.findById(id);
        targetProduct.modify(request);
        productService.saveProduct(targetProduct);
    }

    @GetMapping("/{id}")
    public ProductResponse productDetail(@PathVariable("id") long id) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }

        final var product = productService.findById(id);
        return ProductResponse.from(product);
    }

    @DeleteMapping("/{id}")
    public void productRemove(@PathVariable("id") long id) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }

        productService.removeProduct(id);
    }
}
