package com.example.customermanageproejctproduct.dto;

import com.example.customermanageproejctproduct.category.ProductCategory;
import com.example.customermanageproejctproduct.status.ProductStatus;
import lombok.Getter;

@Getter
public class AddProductRequest {
    private String productName;
    private ProductCategory category;
    private int price;
    private int stock;
    private ProductStatus status;
}
