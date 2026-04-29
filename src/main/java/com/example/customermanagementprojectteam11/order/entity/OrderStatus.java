package com.example.customermanagementprojectteam11.order.entity;

public enum OrderStatus {
    PENDING("준비중"),
    SHIPPING("배송중"),
    DELIVERED("배송완료"),
    CANCELED("취소됨");


    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
