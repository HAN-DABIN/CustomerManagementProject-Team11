package com.example.customermanagementprojectteam11.product.dto;

import com.example.customermanagementprojectteam11.product.category.ProductCategory;
import com.example.customermanagementprojectteam11.product.status.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GetAllProductRequest {
    @NotBlank(message = "상품명을 입력하세요")
    private String productName;

    @NotNull(message = "카테고리를 입력하세요")
    private ProductCategory category;

    @NotNull(message = "상품상태를 입력하세요")
    private ProductStatus status;
}
