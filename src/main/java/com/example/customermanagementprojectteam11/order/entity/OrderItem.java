package com.example.customermanagementprojectteam11.order.entity;

import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.customer.entity.Customer;
import com.example.customermanagementprojectteam11.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_itmes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // customer엔티티와 연관관계 1:N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custormer_id")
    private Customer customer;
    // product엔티티와 연관관계 1:N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    // admin엔티티와 연관관계 1:N
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id")
    private Admin admin;
    @Column(nullable = false)
    private Long quantity;
    @Column(name = "total_price", nullable = false)
    private Long totalPrice;


}
