package com.waterfogsw.gcccoffee.product.model;

import com.waterfogsw.gcccoffee.product.controller.dto.ProductAddRequest;

import java.time.LocalDateTime;

public class Product {
    private final long id;
    private String name;
    private Category category;
    private long price;
    private String description;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product(long id, String name, Category category, long price, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Product from(ProductAddRequest request) {
        return new Builder(0)
                .name(request.name())
                .category(request.category())
                .price(request.price())
                .description(request.description())
                .build();
    }

    public Product(Builder builder) {
        this(builder.id, builder.name, builder.category, builder.price, builder.description);
    }

    static public class Builder {
        private final long id;
        private String name;
        private Category category;
        private long price;
        private String description;

        public Builder(long id) {
            this.id = id;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public Builder price(long price) {
            this.price = price;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public long getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
