package com.example.customermanagementprojectteam11.product.dto;

import com.example.customermanagementprojectteam11.product.category.ProductCategory;
import com.example.customermanagementprojectteam11.product.status.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class GetAllProductRequest {
    @NotBlank(message = "상품명을 입력하세요")
    private String productName;

    @NotBlank(message = "카테고리를 입력하세요")
    private ProductCategory category;

    @NotBlank(message = "수량을 입력하세요")
    @Size(min = 1)
    private ProductStatus status;
}
