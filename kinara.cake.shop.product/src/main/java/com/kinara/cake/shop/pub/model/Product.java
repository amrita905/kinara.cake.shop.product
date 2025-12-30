package com.kinara.cake.shop.pub.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(
    name = "products",
    indexes = {
        @Index(name = "idx_products_category", columnList = "category"),
        @Index(name = "idx_products_active", columnList = "active")
    }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductCategory category; // CAKE / CUPCAKE

    @Column(nullable = false)
    private int availableStock;

    @Column(nullable = false)
    private boolean active = true;

    /**
     * Used for optimistic locking.
     * Prevents overselling when multiple users order simultaneously.
     */
    @Version
    private Long version;

    // ----------------- Constructors -----------------

    public Product() {}

    public Product(String name, BigDecimal price,
                   ProductCategory category, int availableStock) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.availableStock = availableStock;
        this.active = true;
    }

    // ----------------- Getters & Setters -----------------

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getVersion() {
        return version;
    }
}
