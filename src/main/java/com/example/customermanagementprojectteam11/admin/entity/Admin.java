package com.example.customermanagementprojectteam11.admin.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "admins")
public class Admin {
    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(length = 200, unique = true, nullable = false)
    private String email;
    @Column(length = 255, nullable = false)
    private String password;
    @Column(length = 20, nullable = false)
    private String role; // 슈퍼관리자, 운영관리자, CS관리자
    @Column(length = 20, nullable = false)
    private String status; // 활성, 비활성, 정지, 승인대기, 거부
    @Column(length = 20, nullable = false)
    private String phoneNumber;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt; // 관리자 생성일
    @Column(name = "approved_at")
    private LocalDateTime approvedAt; // 관리자 승인일
    @LastModifiedDate
    private LocalDateTime modifiedAt; // 관리자 정보 수정일
    @Column(length = 50)
    private String rejectReason; // 거부 사유
    private LocalDateTime rejectedAt; // 거부 일자

    // 생성자
    protected Admin() {}

    public Admin(String name, String email, String password, String role, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = "승인대기"; // 최초 생성 시 승인대기 상태
        this.phoneNumber = phoneNumber;
    }
    // 역할 - 슈퍼관리자
    public void assignSuperAdmin() {
        this.role = "슈퍼관리자";
    }

    // 역할 - 운영관리자
    public void assignOperationAdmin() {
        this.role = "운영관리자";
    }

    // 역할 - CS 관리자
    public void assignCsAdmin() {
        this.role = "CS관리자";
    }

    // 상태 - 활성 + 승인 시간
    public void activate() {
        this.status = "활성";
        this.approvedAt = LocalDateTime.now(); // 활성 -> 승인 승인날짜 업데이트
    }
    // 상태 - 비활성
    public void deactivate() {
        this.status = "비활성";
    }
    // 상태 - 정지
    public void suspend() {
        this.status = "정지";
    }
    // 상태 - 거부
    public void reject(String reason) {
        this.status = "거부";
        this.rejectReason = reason;
        this.rejectedAt = LocalDateTime.now(); // 거부 일자 업데이트
    }


    // 기능
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public LocalDateTime getRejectedAt() {
        return rejectedAt;
    }
}
