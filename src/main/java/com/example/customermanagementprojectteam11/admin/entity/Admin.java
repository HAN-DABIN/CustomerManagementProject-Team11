package com.example.customermanagementprojectteam11.admin.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "admins")
public class Admin extends BaseEntity {
    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(length = 200, unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // 슈퍼관리자, 운영관리자, CS관리자
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING; // 활성, 비활성, 정지, 승인대기, 거부 -> 초기값 승인대기
    @Column(length = 20, nullable = false)
    private String phoneNumber;
    @Column(length = 50)
    private String rejectReason; // 거부 사유


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

    public Role getRole() {
        return role;
    }

    public Status getStatus() {
        return status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public String getRejectReason() {
        return rejectReason;
    }
}
