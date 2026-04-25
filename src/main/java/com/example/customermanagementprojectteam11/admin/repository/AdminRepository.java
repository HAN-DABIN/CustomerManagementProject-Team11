package com.example.customermanagementprojectteam11.admin.repository;

import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    // 검색키워드
    Page<Admin> findByNameContainingOrEmailContaining (
            String name,
            String email,
            Pageable pageable);

    // 역할 필터
    Page<Admin> findByRole(AdminRole role, Pageable pageable);

    // 검색 + 필터
    Page<Admin> findAll(
            String name,
            String email,
            AdminRole role,
            Pageable pageable);
}
