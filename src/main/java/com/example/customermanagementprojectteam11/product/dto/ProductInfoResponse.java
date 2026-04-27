package com.example.customermanagementprojectteam11.product.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ProductInfoResponse {
    private final List<GetAllProductResponse> response;
    private final ProductPageableResponse pageableResponse;

    public ProductInfoResponse(List<GetAllProductResponse> response, ProductPageableResponse pageableResponse) {
        this.response = response;
        this.pageableResponse = pageableResponse;
    }
}
