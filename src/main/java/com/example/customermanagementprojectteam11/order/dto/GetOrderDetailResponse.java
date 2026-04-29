package com.example.customermanagementprojectteam11.order.dto;

import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import com.example.customermanagementprojectteam11.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetOrderDetailResponse {
    private final Long id;
    // 오더아이템 주문번호
    private final Long orderNumber;
    // 오더아이템 주문자명
    private final String customerName;
    // 오더아이템 주문자 이메일
    private final String customerEmail;
    // 오더아이템 상품명
    private final String productName;
    // ????
    private final Long quantity;
    private final Long totalPrice;
    private final LocalDateTime createdAt;
    private final OrderStatus status;
    // 오더아이템 관리자 리스트 - null 허용
    private final String adminName;
    private final String adminEmail;
    private final AdminRole role;
}
