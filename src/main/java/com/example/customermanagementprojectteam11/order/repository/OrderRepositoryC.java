package com.example.customermanagementprojectteam11.order.repository;

import com.example.customermanagementprojectteam11.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepositoryC extends JpaRepository<Order, Long> {
}
