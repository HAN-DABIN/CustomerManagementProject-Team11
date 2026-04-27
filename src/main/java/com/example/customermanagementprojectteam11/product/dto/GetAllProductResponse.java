package com.example.customermanagementprojectteam11.product.dto;

import com.example.customermanagementprojectteam11.product.category.ProductCategory;
import com.example.customermanagementprojectteam11.product.status.ProductStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetAllProductResponse {
    private final Long productId;
    private final String productName;
    private final ProductCategory category;
    private final long price;
    private final long stock;
    private final ProductStatus status;
    private final String userName;
    private final LocalDateTime createAt;

    public GetAllProductResponse(Long productId, String productName, ProductCategory category, long price, long stock, ProductStatus status, String userName, LocalDateTime createAt) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.userName = userName;
        this.createAt = createAt;
    }
}
