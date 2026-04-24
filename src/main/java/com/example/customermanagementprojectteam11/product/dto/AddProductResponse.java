package com.example.customermanagementprojectteam11.product.dto;

import com.example.customermanagementprojectteam11.product.category.ProductCategory;
import com.example.customermanagementprojectteam11.product.status.ProductStatus;
import lombok.Getter;

@Getter
public class AddProductResponse {
    private final String productName;
    private final ProductCategory category;
    private final long price;
    private final long stock;
    private final ProductStatus status;

    public AddProductResponse(String productName, ProductCategory category, long price, long stock, ProductStatus status) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }
}
