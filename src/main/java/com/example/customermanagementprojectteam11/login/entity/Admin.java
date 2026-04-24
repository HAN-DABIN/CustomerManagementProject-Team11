package com.example.customermanagementprojectteam11.login.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "admins")
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdminRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdminStatus status;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt; // 가입 승인일

    @Column(name = "reject_reason", length = 50)
    private String rejectReason; // 가입 승인 거부 사유

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt; // 가입 승인 거부 일자

    public boolean canLogin() {
        return this.status == AdminStatus.ACTIVE;    //로그인이 가능한 상태인지 판별
    }

    //로그인 실패 시 메시지
    public String loginMessage() {
        if (this.canLogin()) return null;
        return this.status.getDescription(); // 로그인 안될 때 이유 반환
    }




}
