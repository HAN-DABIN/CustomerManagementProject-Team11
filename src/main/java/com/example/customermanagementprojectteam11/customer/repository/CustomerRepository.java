package com.example.customermanagementprojectteam11.customer.repository;

import com.example.customermanagementprojectteam11.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {


    //1. 이름OR이메일 검색
    Page<Customer> findByNameContainingOrEmailContaining(String nameKeyword, String emailKeyword, Pageable pageable);

    //2. 상태만 조회(엔티티 속성
    Page<Customer> findByStatus(String status, Pageable pageable);

    //3. 상태 + 이름/이메일 검색
    Page<Customer> findByStatusAndNameContainingOrStatusAndEmailContaining(
            String status1, String nameKeyword, String status2, String emailKeyword, Pageable pageable
    );

    //4.예시 오;; 스프링이 메서드 만들어주기 이메일 찾아주기
    List<Customer> findByEmail(String email);
}
