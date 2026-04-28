package com.example.customermanagementprojectteam11.customermanageproejctproduct.dto;

import com.example.customermanagementprojectteam11.customermanageproejctproduct.category.ProductCategory;
import com.example.customermanagementprojectteam11.customermanageproejctproduct.status.ProductStatus;
import lombok.Getter;

@Getter
public class AddProductRequest {
    private String productName;
    private ProductCategory category;
    private long price;
    private long stock;
    private ProductStatus status;
}
