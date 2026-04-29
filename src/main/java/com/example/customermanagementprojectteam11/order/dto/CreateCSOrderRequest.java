package com.example.customermanagementprojectteam11.order.dto;

import com.example.customermanagementprojectteam11.product.category.ProductCategory;
import com.example.customermanagementprojectteam11.product.status.ProductStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateCSOrderRequest {
    private Long customerid;
    private Long productId;

    @NotNull(message = "수량을 입력하세요")
    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
    private Long stock;


}
