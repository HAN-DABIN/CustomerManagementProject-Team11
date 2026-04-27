package com.example.customermanagementprojectteam11.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email); //이메일로 정보 가져옴
    boolean existsByEmail(String email);   // 회원가입 요구사항이지만 일단 추가 (이메일 중복불가 처리)
}
