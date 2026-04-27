package com.example.customermanagementprojectteam11.customer.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class ListCustomerResponse {

    //현재 페이지
    private final int currentPage;

    //페이지당 개수
    private final int pageSize;

    //전체 개수
    private final int totalCount;

    //전체 페이지 수
    private final int totalPages;

    //고객 리스트
    private final List<GetCustomerResponse> customers;

    public ListCustomerResponse(int currentPage, int pageSize, int totalCount, int totalPages, List<GetCustomerResponse> customers) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPages = totalPages;
        this.customers = customers;
    }
}
