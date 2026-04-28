package com.example.customermanagementprojectteam11.admin.repository;

import com.example.customermanagementprojectteam11.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface AdminRepository
        extends JpaRepository<Admin, Long>,
        JpaSpecificationExecutor<Admin> {

    Optional<Admin> findByEmail(String email);
}
