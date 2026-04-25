package com.example.customermanagementprojectteam11.admin.repository;

import com.example.customermanagementprojectteam11.admin.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Page<Admin> findByNameContainingOrEmailContaining (
            String name,
            String email,
            Pageable pageable);
}
