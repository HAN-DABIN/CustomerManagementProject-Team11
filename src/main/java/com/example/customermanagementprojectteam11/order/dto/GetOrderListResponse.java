package com.example.customermanagementprojectteam11.order.dto;

import com.example.customermanagementprojectteam11.order.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetOrderListResponse {

    private int currentPage;
    private int pageSize;
    private long totalCount;
    private int totalPages;
    private List<GetOrderListResponse.OrderDto> adminList;

    @Getter
    @AllArgsConstructor
    public static class OrderDto {
        private final Long id;
        private final Long orderNumber;
        // 고객명 - 주문 당시 고객 이름 -> 오더아이템
        private final String customerName;
        // 상품명 - 주문 당시 상품 이름 -> 오더아이템
        private final String productName;
        // 수량 - 오더아이템
        private final Long quantity;
        // 금액 - 주문 당시 금액
        private final Long totalPrice;
        private final LocalDateTime createdAt;
        private final OrderStatus status;
        // 관리자명 - 주문 당시 관리자 이름 -> 오더아이템
        private final String adminName;
    }
}
