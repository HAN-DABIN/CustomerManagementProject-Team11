package com.example.customermanagementprojectteam11.order.dto;

import com.example.customermanagementprojectteam11.order.entity.OrderStatus;
import lombok.Getter;

@Getter
public class StatusUpdateRequest {

   private OrderStatus status;

}
