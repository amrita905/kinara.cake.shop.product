package com.kinara.cake.shop.pub.repo;

import com.kinara.cake.shop.pub.model.Product;
import com.kinara.cake.shop.pub.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    List<Product> findByCategoryAndActiveTrue(ProductCategory category);
}

