package com.example.customermanagementprojectteam11.order.controller;

import com.example.customermanagementprojectteam11.order.dto.StatusUpdateRequest;
import com.example.customermanagementprojectteam11.order.dto.StatusUpdateResponse;
import com.example.customermanagementprojectteam11.order.entity.OrderStatus;
import com.example.customermanagementprojectteam11.order.service.OrderServiceUD;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderControllerUD {

    private final OrderServiceUD orderServiceUD;

    //주문 상태 수정
    @PatchMapping("/{id}")
    public ResponseEntity<StatusUpdateResponse> orderStatusUpdate(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(orderServiceUD.orderStatusUpdate(id, request));
    }
}
