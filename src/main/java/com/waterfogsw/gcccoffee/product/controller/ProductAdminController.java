package com.waterfogsw.gcccoffee.product.controller;

import com.waterfogsw.gcccoffee.product.controller.dto.ProductAddRequest;
import com.waterfogsw.gcccoffee.product.controller.dto.ProductResponse;
import com.waterfogsw.gcccoffee.product.model.Product;
import com.waterfogsw.gcccoffee.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ProductAdminController {

    private final ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String productPage(Model model) {
        List<Product> products = productService.findAllProduct();
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("/products/new")
    public String newProduct() {
        return "new-product";
    }

    @PostMapping("/products/new")
    public String newProduct(ProductAddRequest request) {
        productService.addProduct(Product.from(request));
        return "redirect:/products";
    }

    @GetMapping("/products/{id}")
    public String viewProductPage(@PathVariable long id, Model model) {
        final var product = productService.findById(id);
        model.addAttribute("product", ProductResponse.from(product));
        return "product";
    }
}
