package com.example.customermanagementprojectteam11.order.entity;

import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.common.BaseEntity;
import com.example.customermanagementprojectteam11.customer.entity.Customer;
import com.example.customermanagementprojectteam11.product.category.ProductCategory;
import com.example.customermanagementprojectteam11.product.status.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // orderItem 엔티티와 연관관계 1:N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;
    // customer엔티티와 연관관계 1:N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    // admin엔티티와 연관관계 1:N
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id")
    private Admin admin;
    @Column(name = "order_number", nullable = false)
    private Long orderNumber;
    @Column(nullable = false)
    private OrderStatus orderStatus;
    @Column(name = "unit_price", nullable = false)
    private Long unitPrice;
    @Column(name = "total_price",nullable = false)
    private Long totalPrice;

    // 주문 생성 시 사용하는 속성
    private String name;

    private String email;

    private String phoneNumber;

    private String productName;

    private ProductCategory category;

    private Long price;

    private Long stock;

    private ProductStatus productStatus;


    public Order(String name, String email, String phoneNumber, String productName, ProductCategory category, Long price, Long stock, Long orderNumber, OrderStatus orderStatus, ProductStatus productStatus){
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.productStatus = productStatus;
    }


}
