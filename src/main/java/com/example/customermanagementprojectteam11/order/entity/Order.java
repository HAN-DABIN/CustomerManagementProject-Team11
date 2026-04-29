package com.example.customermanagementprojectteam11.order.entity;

import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.common.BaseEntity;
import com.example.customermanagementprojectteam11.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "oreders")
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
    @JoinColumn(name = "custormer_id")
    private Customer customer;
    // admin엔티티와 연관관계 1:N
    @ManyToOne(fetch = FetchType.LAZY)
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



}
