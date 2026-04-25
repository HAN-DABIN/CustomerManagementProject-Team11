package com.example.customermanagementprojectteam11.admin.repository;

import com.example.customermanagementprojectteam11.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    List<Admin> findByNameContainingOrEmailContaining (String name, String email);
}
