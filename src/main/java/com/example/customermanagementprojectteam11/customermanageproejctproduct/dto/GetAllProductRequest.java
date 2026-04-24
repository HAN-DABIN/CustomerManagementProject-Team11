package com.example.customermanagementprojectteam11.customermanageproejctproduct.dto;

import com.example.customermanagementprojectteam11.customermanageproejctproduct.category.ProductCategory;
import com.example.customermanagementprojectteam11.customermanageproejctproduct.status.ProductStatus;
import lombok.Getter;

@Getter
public class GetAllProductRequest {
    private String productName;
    private ProductCategory category;
    private ProductStatus status;
}
