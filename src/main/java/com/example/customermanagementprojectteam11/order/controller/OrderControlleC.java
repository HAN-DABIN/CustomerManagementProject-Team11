package com.example.customermanagementprojectteam11.order.controller;

import com.example.customermanagementprojectteam11.order.dto.CreateCSOrderRequest;
import com.example.customermanagementprojectteam11.order.dto.CreateCSOrderResponse;
import com.example.customermanagementprojectteam11.order.service.OrderServiceC;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/csorder")
public class OrderControlleC {
    private final OrderServiceC csorderServiceC;

    @PostMapping
    public ResponseEntity<CreateCSOrderResponse> registOrder(@Valid @RequestBody CreateCSOrderRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(csorderServiceC.regist(request));
    }
}
