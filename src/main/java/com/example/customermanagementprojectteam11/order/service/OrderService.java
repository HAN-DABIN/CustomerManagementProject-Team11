package com.example.customermanagementprojectteam11.order.service;

import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import com.example.customermanagementprojectteam11.common.exception.BadRequestException;
import com.example.customermanagementprojectteam11.common.exception.NotFoundException;
import com.example.customermanagementprojectteam11.common.exception.BadRequestException;
import com.example.customermanagementprojectteam11.common.exception.ProductNotAvailableException;
import com.example.customermanagementprojectteam11.customer.entity.Customer;
import com.example.customermanagementprojectteam11.customer.repository.CustomerRepository;
import com.example.customermanagementprojectteam11.order.dto.*;
import com.example.customermanagementprojectteam11.order.entity.Order;
import com.example.customermanagementprojectteam11.order.entity.OrderItem;
import com.example.customermanagementprojectteam11.order.entity.OrderStatus;
import com.example.customermanagementprojectteam11.order.repository.OrderRepository;
import com.example.customermanagementprojectteam11.product.entity.Product;
import com.example.customermanagementprojectteam11.product.repository.ProductRepository;
import com.example.customermanagementprojectteam11.product.status.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CreateCSOrderResponse regist(CreateCSOrderRequest request) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
        int randomNum = (int)(Math.random() * 900) + 100; // 100~999 사이의 랜덤 숫자
        Long generatedOrderNumber = Long.parseLong(timestamp + randomNum);

        Customer customer = customerRepository.findById(request.getCustomerid()).orElseThrow(
                () -> new NotFoundException("해당 사용자를 찾을 수 없습니다.")
        );
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new NotFoundException("존재하지 않는 상품입니다.")
        );
        if (request.getStock() < 1) {
            throw new BadRequestException("주문 수량은 최소 1개 이상이어야 합니다.");
        }
        if (product.getStatus() == ProductStatus.DISCONTINUED || product.getStatus() == ProductStatus.SOLD_OUT) {
            throw new ProductNotAvailableException("주문할 수 없는 상품 상태입니다.");
        }
        Order order = new Order(
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                product.getProductName(),
                product.getCategory(),
                product.getPrice(),
                request.getStock(),
                generatedOrderNumber,
                OrderStatus.PENDING,
                product.getStatus(),
                product.getPrice() * request.getStock());
        product.removeStock(request.getStock());
        Order savedOrder = orderRepository.save(order);
        return new CreateCSOrderResponse(
                savedOrder.getName(),
                savedOrder.getEmail(),
                savedOrder.getPhoneNumber(),
                savedOrder.getProductName(),
                savedOrder.getCategory(),
                savedOrder.getTotalPrice(),
                savedOrder.getCreatedAt(),
                savedOrder.getOrderNumber(),
                savedOrder.getOrderStatus(),
                savedOrder.getProductStatus());
    }

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
        Page<Order> orderPage = orderRepository.findAll(spec, pageable);

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
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("주문내역이 없습니다."));

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

    //주문 상태 수정
    //조건1. 특정 주문 조회
    //조건2. 존재 없으면 에러
    //조건3. 현재 상태 확인
    //조건4. 상태를 준비중 -> 배송중 -> 배송완료 순서로만 변경 허용
    //조건5. 순서 어기면 예외

    //ID 찾고 예외처리 메서드
    public Order findByIdOrThrow(Long id) {
        return orderRepository.findById(id).orElseThrow(
                //없는 고객 조회시 404 에러 뜨게 변경
                () -> new NotFoundException("해당 주문을 찾을 수 없습니다."));
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
            throw new BadRequestException("변경할 수 없는 주문 상태입니다.");
        }

        // 5. 응답 반환
        return new StatusUpdateResponse(order.getOrderStatus());
    }

    //주문 취소 시 재고 처리
    //Delete 지만 로직 자체는 Patch
    @Transactional
    public OrderCancelResponse orderCancel(Long id, OrderCancelRequest request) {

        //1. 주문 id로 주문 조회하기
        Order order = findByIdOrThrow(id);

        //2. 요청으로 받은 취소 사유를 꺼내준다.
        String cancelReason = request.getCancelReason();

        //3. 취소 사유가 비어있을 경우 예외 처리 진행하기
        if (cancelReason == null || cancelReason.isBlank()) {
            throw new BadRequestException("취소 사유는 필수 입력입니다.");
        }

        //4. 현재 주문 상태 확인하기
        OrderStatus status = order.getOrderStatus();

        //5. 현재 상태가 준비중이 아닐 경우 예외를 던져야한다.
        //다른 상황일 경우 취소 불가능 처리
        if (status != OrderStatus.PENDING) {
            throw new BadRequestException("준비중 상태에서만 취소 가능합니다.");
        }

        //6. 주문에 연결된 주문 상품을 꺼내준다. OrderItem
        //CANCELED로 바꾸는 게 끝이 아닌 재고를 다시 돌려놔야 하기 때문에 해당 구조 이용
        OrderItem orderItem = order.getOrderItem();

        //6-1 주문 상품 정보가 없을 때 예외처리 진행
        if (orderItem == null) {
            throw new BadRequestException("주문 상품 정보가 없습니다.");
        }

        //7. 주문 상품에서 상품과 수량을 꺼내준다.
        //재고 복구할 대상 상품+수량을 찾는 것
        Product product = orderItem.getProduct();
        Long quantity = orderItem.getQuantity();

        //8. 주문 상태를 취소됨으로 바꾼 뒤 취소 사유까지 저장해준다.
        order.cancelOrder(cancelReason);

        //9. 상품 재고를 주문의 수량만큼 복구해준다.
        product.addStock(quantity);

        //10. 응답 반환
        return new OrderCancelResponse(order.getOrderStatus(), order.getCancelReason());
    }
}
