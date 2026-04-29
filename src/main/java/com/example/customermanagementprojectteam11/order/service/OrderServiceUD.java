package com.example.customermanagementprojectteam11.order.service;

import com.example.customermanagementprojectteam11.customer.entity.Customer;
import com.example.customermanagementprojectteam11.order.dto.StatusUpdateRequest;
import com.example.customermanagementprojectteam11.order.dto.StatusUpdateResponse;
import com.example.customermanagementprojectteam11.order.entity.Order;
import com.example.customermanagementprojectteam11.order.entity.OrderStatus;
import com.example.customermanagementprojectteam11.order.repository.OrderRepositoryUD;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class OrderServiceUD {

    private final OrderRepositoryUD orderRepositoryUD;

    //주문 상태 수정
    //조건1. 특정 주문 조회
    //조건2. 존재 없으면 에러
    //조건3. 현재 상태 확인
    //조건4. 상태를 준비중 -> 배송중 -> 배송완료 순서로만 변경 허용
    //조건5. 순서 어기면 예외

    //ID 찾고 예외처리 메서드
    public Order findByIdOrThrow(Long id) {
        return orderRepositoryUD.findById(id).orElseThrow(
                //없는 고객 조회시 404 에러 뜨게 변경
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다."));
    }

    //주문 상태 수정
    @Transactional
    public StatusUpdateResponse orderStatusUpdate(Long id, StatusUpdateRequest request) {

        //1. 주문 id로 주문 조회하기
        Order order = findByIdOrThrow(id);

        //2. 요청으로 받은 새 상태 꺼내기
        OrderStatus newStatus = request.getStatus();

        //3. 현재 상태 확인하기
        OrderStatus currentStatus = order.getOrderStatus();

        //4. 상태가 변경 가능한 상태인지 검증
        //상황1. 상태가 준비중이고 바꾸려는 것이 배송중이라면 변경 가능
        if (currentStatus == OrderStatus.PENDING && newStatus == OrderStatus.SHIPPING) {
            order.changeStatus(newStatus);
        }
        //상황2. 상태가 배송중이고 바꾸려는 것이 배송완료라면 변경 가능
        else if (currentStatus == OrderStatus.SHIPPING && newStatus == OrderStatus.DELIVERED) {
            order.changeStatus(newStatus);
        }
        //상황3. 두 경우가 모두 아닐 시 던짐
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "변경할 수 없는 주문 상태입니다.");
        }

        // 5. 응답 반환
        return new StatusUpdateResponse(order.getOrderStatus());


    }
}
