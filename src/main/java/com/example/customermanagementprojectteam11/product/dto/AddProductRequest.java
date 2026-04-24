package com.example.customermanagementprojectteam11.product.dto;

import com.example.customermanagementprojectteam11.product.category.ProductCategory;
import com.example.customermanagementprojectteam11.product.status.ProductStatus;
import lombok.Getter;

@Getter
public class AddProductRequest {
    private String productName;
    private ProductCategory category;
    private long price;
    private long stock;
    private ProductStatus status;
}
