package com.example.customermanagementprojectteam11.product.entity;

import com.example.customermanagementprojectteam11.BaseEntity;
import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.product.category.ProductCategory;
import com.example.customermanagementprojectteam11.product.status.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

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

    public void statusUpdate(ProductStatus status){
        this.status = status;
    }
}
