package com.example.customermanagementprojectteam11.order.repository;

import com.example.customermanagementprojectteam11.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
