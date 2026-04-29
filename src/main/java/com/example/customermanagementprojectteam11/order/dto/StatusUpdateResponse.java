package com.example.customermanagementprojectteam11.order.dto;

import com.example.customermanagementprojectteam11.order.entity.OrderStatus;
import lombok.Getter;

@Getter
public class StatusUpdateResponse {

    private final OrderStatus status;

    public StatusUpdateResponse(OrderStatus status) {
        this.status = status;
    }
}
