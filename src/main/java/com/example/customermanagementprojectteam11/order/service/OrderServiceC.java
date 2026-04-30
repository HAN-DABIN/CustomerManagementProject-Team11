package com.example.customermanagementprojectteam11.order.service;

import com.example.customermanagementprojectteam11.customer.entity.Customer;
import com.example.customermanagementprojectteam11.customer.repository.CustomerRepository;
import com.example.customermanagementprojectteam11.order.dto.CreateCSOrderRequest;
import com.example.customermanagementprojectteam11.order.dto.CreateCSOrderResponse;
import com.example.customermanagementprojectteam11.order.entity.Order;
import com.example.customermanagementprojectteam11.order.entity.OrderStatus;
import com.example.customermanagementprojectteam11.order.repository.OrderRepositoryC;
import com.example.customermanagementprojectteam11.product.entity.Product;
import com.example.customermanagementprojectteam11.product.handler.ProductNotFoundException;
import com.example.customermanagementprojectteam11.product.handler.ProductStatusErrorException;
import com.example.customermanagementprojectteam11.product.repository.ProductRepository;
import com.example.customermanagementprojectteam11.product.status.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



@Service
@RequiredArgsConstructor
public class OrderServiceC {
    private final OrderRepositoryC orderRepositoryC;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CreateCSOrderResponse regist(CreateCSOrderRequest request) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
        int randomNum = (int)(Math.random() * 900) + 100; // 100~999 사이의 랜덤 숫자
        Long generatedOrderNumber = Long.parseLong(timestamp + randomNum);

        Customer customer = customerRepository.findById(request.getCustomerid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다.")
        );
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new ProductNotFoundException("존재하지 않는 상품입니다.")
        );
        if (request.getStock() < 1) {
            throw new IllegalArgumentException("주문 수량은 최소 1개 이상이어야 합니다.");
        }
        if (product.getStatus() == ProductStatus.DISCONTINUED || product.getStatus() == ProductStatus.SOLD_OUT) {
            throw new ProductStatusErrorException("주문할 수 없는 상품 상태입니다.");
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
        Order savedOrder = orderRepositoryC.save(order);
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
}
