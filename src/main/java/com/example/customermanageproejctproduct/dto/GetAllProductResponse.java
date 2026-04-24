package com.example.customermanageproejctproduct.dto;

import com.example.customermanageproejctproduct.category.ProductCategory;
import com.example.customermanageproejctproduct.status.ProductStatus;
import lombok.Getter;

@Getter
public class GetAllProductResponse {
    private final Long productId;
    private final String productName;
    private final ProductCategory category;
    private final long price;
    private final long stock;
    private final ProductStatus status;
    private final String userName;

    public GetAllProductResponse(Long productId, String productName, ProductCategory category, long price, long stock, ProductStatus status, String userName) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.userName = userName;
    }
}
