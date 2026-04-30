package com.example.customermanagementprojectteam11.order.repository;

import com.example.customermanagementprojectteam11.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository extends JpaRepository<Order, Long>,
        JpaSpecificationExecutor<Order> {
}
