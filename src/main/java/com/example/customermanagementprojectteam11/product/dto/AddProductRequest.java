package com.example.customermanagementprojectteam11.product.dto;

import com.example.customermanagementprojectteam11.product.category.ProductCategory;
import com.example.customermanagementprojectteam11.product.status.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddProductRequest {
    @NotBlank(message = "상품명을 입력하세요")
    private String productName;

    @NotNull(message = "카테고리를 입력하세요")
    private ProductCategory category;

    @NotNull(message = "가격을 입력하세요")
    @Min(value = 100, message = "가격은 최소 100원 이상이어야 합니다.")
    private Long price;

    @NotNull(message = "수량을 입력하세요")
    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
    private Long stock;

    @NotNull(message = "상품 상태를 입력하세요")
    private ProductStatus status;
}
