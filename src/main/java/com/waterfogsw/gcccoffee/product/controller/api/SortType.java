package com.waterfogsw.gcccoffee.product.controller.api;

import com.waterfogsw.gcccoffee.product.model.Product;

import java.util.Comparator;

public enum SortType {
    PRICE_ASC(Comparator.comparing(Product::getPrice)),
    PRICE_DESC(Comparator.comparing(Product::getPrice).reversed()),
    DATE_ASC(Comparator.comparing(Product::getCreatedAt)),
    DATE_DESC(Comparator.comparing(Product::getCreatedAt).reversed());

    private final Comparator<Product> comparator;

    SortType(Comparator<Product> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Product> getComparator() {
        return comparator;
    }
}
