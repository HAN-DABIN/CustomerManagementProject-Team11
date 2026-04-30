package com.example.customermanagementprojectteam11.order.service;

import com.example.customermanagementprojectteam11.order.dto.OrderCancelRequest;
import com.example.customermanagementprojectteam11.order.dto.StatusUpdateRequest;
import com.example.customermanagementprojectteam11.order.dto.StatusUpdateResponse;
import com.example.customermanagementprojectteam11.order.entity.Order;
import com.example.customermanagementprojectteam11.order.entity.OrderItem;
import com.example.customermanagementprojectteam11.order.entity.OrderStatus;
import com.example.customermanagementprojectteam11.order.repository.OrderRepositoryUD;
import com.example.customermanagementprojectteam11.product.entity.Product;
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

    //주문 취소 시 재고 처리
    //Delete 지만 로직 자체는 Patch
    @Transactional
    public void orderCancel(Long id, OrderCancelRequest request) {

        //1. 주문 id로 주문 조회하기
        Order order = findByIdOrThrow(id);

        //2. 요청으로 받은 취소 사유를 꺼내준다.
        String cancelReason = request.getCancelReason();

        //3. 취소 사유가 비어있을 경우 예외 처리 진행하기
        if (cancelReason == null || cancelReason.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "취소 사유는 필수 입력입니다.");
        }

        //4. 현재 주문 상태 확인하기
        OrderStatus status = order.getOrderStatus();

        //5. 현재 상태가 준비중이 아닐 경우 예외를 던져야한다.
        //다른 상황일 경우 취소 불가능 처리
        if (status != OrderStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "준비중 상태에서만 취소 가능합니다.");
        }

        //6. 주문에 연결된 주문 상품을 꺼내준다. OrderItem
        //CANCELED로 바꾸는 게 끝이 아닌 재고를 다시 돌려놔야 하기 때문에 해당 구조 이용
        OrderItem orderItem = order.getOrderItem();

        //7. 주문 상품에서 상품과 수량을 꺼내준다.
        //재고 복구할 대상 상품+수량을 찾는 것
        Product product = orderItem.getProduct();
        Long quantity = orderItem.getQuantity();

        //8. 주문 상태를 취소됨으로 바꾼 뒤 취소 사유까지 저장해준다.
        order.cancelOrder(cancelReason);

        //9. 상품 재고를 주문의 수량만큼 복구해준다.
        product.addStock(quantity);
    }
}
