package com.example.customermanageproejctproduct.repository;

import com.example.customermanageproejctproduct.category.ProductCategory;
import com.example.customermanageproejctproduct.dto.GetAllProductRequest;
import com.example.customermanageproejctproduct.entity.Product;
import com.example.customermanageproejctproduct.status.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE " +
            "(:productName IS NULL OR p.productName = :productName) AND " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:status IS NULL OR p.status = :status)")
    Page<Product> findAll(@Param("productName") String productName,
                          @Param("category") ProductCategory category,
                          @Param("status") ProductStatus status,
                          Pageable pageable);

}
