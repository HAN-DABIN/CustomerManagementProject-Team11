package com.example.customermanagementprojectteam11.customermanageproejctproduct.dto;

import lombok.Getter;

@Getter
public class ProductPageableResponse {
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPage;

    public ProductPageableResponse(int pageNumber, int pageSize, long totalElements, int totalPage) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPage = totalPage;
    }
}
