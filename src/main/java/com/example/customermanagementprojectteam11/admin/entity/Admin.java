package com.example.customermanagementprojectteam11.admin.entity;

import com.example.customermanagementprojectteam11.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
    private AdminRole role; // 슈퍼관리자, 운영관리자, CS관리자
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminStatus status = AdminStatus.PENDING; // 활성, 비활성, 정지, 승인대기, 거부 -> 초기값 승인대기
    @Column(length = 20, nullable = false)
    private String phoneNumber;
    @Column(length = 50)
    private String rejectReason; // 거부 사유

    public boolean canLogin() {
        return this.status == AdminStatus.ACTIVE;    //로그인이 가능한 상태인지 판별
    }

    //로그인 실패 시 메시지
    public String loginMessage() {
        if (this.canLogin()) return null;
        return this.status.getDescription(); // 로그인 안될 때 이유 반환
    }
}
