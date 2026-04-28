package com.example.customermanagementprojectteam11.product.dto;

import com.example.customermanagementprojectteam11.product.status.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateStatusRequest {
    @NotNull(message = "상품 상태를 입력하세요")
    private ProductStatus status;
}
