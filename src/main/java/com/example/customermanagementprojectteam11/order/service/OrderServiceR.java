package com.example.customermanagementprojectteam11.order.service;

import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import com.example.customermanagementprojectteam11.order.dto.GetOrderDetailResponse;
import com.example.customermanagementprojectteam11.order.dto.GetOrderListResponse;
import com.example.customermanagementprojectteam11.order.entity.Order;
import com.example.customermanagementprojectteam11.order.entity.OrderStatus;
import com.example.customermanagementprojectteam11.order.repository.OrderRepositoryR;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceR {
    public final OrderRepositoryR orderRepositoryR;

    // 주문 리스트 조회 기능
    @Transactional(readOnly = true)
    public GetOrderListResponse findList(
            String keyword,
            int page,
            int size,
            String sortBy,
            String direction,
            OrderStatus status) {
        /**
         * 1. 잘못된 page/size 보정
         * 2. 잘못된 sortBy/order 보정
         * 3. 정렬 객체 생성
         * 4. Pageable 생성
         * 5. 검색 조건(specification) 생성
         * 6. keyword / status 조건 추가
         * 7. DB 조회
         */

        // 페이지 기본값 1로 설정
        if (page < 1) page = 1;
        if (size < 1) size = 10;

        // 파라미터 잘못된 값 들어왔을 때 예외처리 (정렬기준)
        switch (sortBy) {
            case "quantity": // 수량
            case "totalPrice": // 금액
            case "createdAt": // 주문일
                break;
            default:
                sortBy = "createdAt";
        }
        // 파라미터 잘못된 값 들어왔을 때 예외처리 (정렬순서)
        switch (direction) {
            case "asc":
            case "desc":
                break;
            default:
                direction = "desc";
        }
        // 정렬 조건 생성
        Sort sort;

        // asc 요청하면 오름차순 정렬
        if (direction.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
            // 그 외 값은 내림차순 정렬
        } else {
            sort = Sort.by(sortBy).descending();
        }
        // pageable 객체 생성
        // 기본값을 1로 설정했으니 내부적 데이터는 0으로 변환하기
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        // Specification 생성 - 빈 조건(전체조회)부터 시작
        Specification<Order> spec = Specification.allOf();

        // 검색키워드가 null이 아니고 비어있지 않을 때 검색조건 추가
        if (keyword != null && !keyword.isBlank()) {
            spec = spec.and(OrderSpecification.keyword(keyword));
        }

        // 상태가 null이 아닐 때 상태필터 추가
        if (status != null) {
            spec = spec.and(OrderSpecification.status(status));
        }

        // 조회
        Page<Order> orderPage = orderRepositoryR.findAll(spec, pageable);

        // 엔티티를 dto로 변환
        List<GetOrderListResponse.OrderDto> orderList = orderPage.getContent().stream()
                .map(order -> {
                    // 주문에 연결된 등록 관리자 정보 조회
                    // 고객 직접 주문 시 admin 값은 null일 수 있음
                    Admin admin = order.getOrderItem().getAdmin();

                    // 관리자 정보 기본값 null로 선언
                    String adminName = null;

                    // 관리자 정보가 있으면 admin에서 정보 가져오기
                    if (admin != null) {
                        adminName = admin.getName();
                    }
                    return new GetOrderListResponse.OrderDto(
                            order.getId(),
                            order.getOrderNumber(),
                            order.getOrderItem().getCustomer().getName(),
                            order.getOrderItem().getProduct().getProductName(),
                            order.getOrderItem().getQuantity(),
                            order.getOrderItem().getTotalPrice(),
                            order.getCreatedAt(),
                            order.getOrderStatus(),
                            adminName
                    );
                }).collect(Collectors.toList());

        // 응답 dto 반환
        return new GetOrderListResponse(
                page,
                size,
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderList
        );

    }

    // 주문 상세 조회 기능
    @Transactional(readOnly = true)
    public GetOrderDetailResponse findDetailOrder(Long orderId) {
        // 엔티티에서 orderId 조회
        Order order = orderRepositoryR.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("주문내역이 없습니다."));

        // 주문에 연결된 등록 관리자 정보 조회
        // 고객 직접 주문 시 admin 값은 null일 수 있음
        Admin admin = order.getAdmin();

        // 관리자 정보 기본값 null로 선언
        String adminName = null;
        String adminEmail = null;
        AdminRole adminRole = null;

        // 관리자 정보가 있으면 admin에서 정보 가져오기
        if (admin != null) {
            adminName = admin.getName();
            adminEmail = admin.getEmail();
            adminRole = admin.getRole();
        }

        // 응답 dto 반환
        return new GetOrderDetailResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getCustomer().getName(),
                order.getCustomer().getEmail(),
                order.getOrderItem().getProduct().getProductName(),
                order.getOrderItem().getQuantity(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getOrderStatus(),
                adminName,
                adminEmail,
                adminRole
        );

    }
}
