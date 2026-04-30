package com.example.customermanagementprojectteam11.order.controller;

import com.example.customermanagementprojectteam11.order.dto.*;
import com.example.customermanagementprojectteam11.order.entity.OrderStatus;
import com.example.customermanagementprojectteam11.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/csorder")
    public ResponseEntity<CreateCSOrderResponse> registOrder(
            @Valid
            @RequestBody CreateCSOrderRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.regist(request));
    }

    // 주문 리스트 조회 API
    @GetMapping
    public ResponseEntity<GetOrderListResponse> findListOrder(
            @RequestParam(required = false) String keyword, // 검색 키워드
            @RequestParam(defaultValue = "1") int page, // 페이지번호, 요청없으면 1페이지
            @RequestParam(defaultValue = "10") int size, // 페이지당 조회 개수, 요청없으면 기본 10개씩 조회
            @RequestParam(defaultValue = "createdAt") String sortBy, // 정렬기준, 기본값: 주문일
            @RequestParam(defaultValue = "desc") String direction, // 정렬방향, 기본값: 내림차순
            @RequestParam(required = false) OrderStatus status){ // 상태필터
        // 서비스에서 받은 결과 반환
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findList(keyword, page, size, sortBy, direction, status));
    }

    // 주문 상세 조회 API
    @GetMapping("/{orderId}")
    public ResponseEntity<GetOrderDetailResponse> findDetailOrder(
            @PathVariable Long orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findDetailOrder(orderId));
    }

    //주문 상태 수정
    @PatchMapping("/{id}")
    public ResponseEntity<StatusUpdateResponse> orderStatusUpdate(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.orderStatusUpdate(id, request));
    }

    //주문 취소 시 재고처리 API
    @DeleteMapping("/{id}")
    public ResponseEntity<OrderCancelResponse> orderCancel(
            @PathVariable Long id,
            @RequestBody OrderCancelRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.orderCancel(id, request));
    }



}
