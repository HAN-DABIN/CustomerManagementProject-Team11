package com.example.customermanagementprojectteam11.product.entity;

import com.example.customermanagementprojectteam11.BaseEntity;
import com.example.customermanagementprojectteam11.product.category.ProductCategory;
import com.example.customermanagementprojectteam11.product.status.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ProductId;

    private String productname;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private Long price;
    private Long stock;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private String userName;
    private String userEmail;

    public Product(String productname, ProductCategory category, long price, long stock, ProductStatus status){
        this.productname = productname;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

    public void update(String productname, ProductCategory category, long price){
        this.productname = productname;
        this.category = category;
        this.price = price;
    }
}
