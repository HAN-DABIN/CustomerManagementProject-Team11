package com.example.customermanagementprojectteam11.product.dto;

import com.example.customermanagementprojectteam11.product.category.ProductCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateProductRequest {
    @NotBlank(message = "상품명을 입력하세요")
    private String productName;

    @NotNull(message = "카테고리를 입력하세요")
    private ProductCategory category;

    @NotNull(message = "가격을 입력하세요")
    @Min(value = 100, message = "최소 100원 이상이어야 합니다")
    private long price;
}
