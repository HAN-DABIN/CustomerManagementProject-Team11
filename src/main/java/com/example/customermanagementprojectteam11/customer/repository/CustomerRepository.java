package com.example.customermanagementprojectteam11.customer.repository;

import com.example.customermanagementprojectteam11.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
