package com.example.customermanageproejctproduct.entity;

import com.example.customermanageproejctproduct.category.ProductCategory;
import com.example.customermanageproejctproduct.status.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ProductId;

    private String productName;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private Long price;
    private Long stock;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    private String userName;
    private String userEmail;

    public Product(String productName, ProductCategory category, long price, long stock, ProductStatus status){
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

    public void update(String productname, ProductCategory category, long price){
        this.productName = productname;
        this.category = category;
        this.price = price;
    }
}
