package com.example.customermanagementprojectteam11.order.dto;

import com.example.customermanagementprojectteam11.order.entity.OrderStatus;
import lombok.Getter;

@Getter
public class OrderCancelResponse {

        private final OrderStatus status;
        private final String cancelReason;

        public OrderCancelResponse(OrderStatus status, String cancelReason) {
            this.status = status;
            this.cancelReason = cancelReason;
        }

}
