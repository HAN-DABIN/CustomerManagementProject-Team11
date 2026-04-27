package com.example.customermanagementprojectteam11.product.dto;

import com.example.customermanagementprojectteam11.product.category.ProductCategory;
import com.example.customermanagementprojectteam11.product.status.ProductStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetOneProductResponse {
    private final String productName;
    private final ProductCategory category;
    private final long price;
    private final long stock;
    private final ProductStatus status;
    private final LocalDateTime createAt;
    private final LocalDateTime updateAt;
    //private final String adminName;
    //private final String adminEmail;

    public GetOneProductResponse(String productName, ProductCategory category, long price, long stock, ProductStatus status, LocalDateTime createAt, LocalDateTime updateAt) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.createAt = createAt;
        this.updateAt = updateAt;
        //this.adminName = adminName;
        //this.adminEmail = adminEmail;
    }
}
