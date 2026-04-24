package com.example.customermanagementprojectteam11.product.repository;

import com.example.customermanagementprojectteam11.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
