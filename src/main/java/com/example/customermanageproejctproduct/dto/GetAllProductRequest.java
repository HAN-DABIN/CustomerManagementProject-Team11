package com.example.customermanageproejctproduct.dto;

import com.example.customermanageproejctproduct.category.ProductCategory;
import com.example.customermanageproejctproduct.status.ProductStatus;
import lombok.Getter;

@Getter
public class GetAllProductRequest {
    private String productName;
    private ProductCategory category;
    private ProductStatus status;
}
