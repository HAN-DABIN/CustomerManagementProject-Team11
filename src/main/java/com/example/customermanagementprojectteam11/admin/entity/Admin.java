package com.example.customermanagementprojectteam11.admin.entity;

import com.example.customermanagementprojectteam11.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.NumericBooleanConverter;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "admins")
@SoftDelete(columnName = "is_deleted", converter = NumericBooleanConverter.class)
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

    // Admin.java 내부
    public Admin(String name, String email, String password, AdminRole role, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.status = AdminStatus.PENDING; // 초기값 설정+ 회원가입 직후에는 로그인 못하게 막음
    }



    public boolean canLogin() {
        return this.status == AdminStatus.ACTIVE;    //로그인이 가능한 상태인지 판별
    }

    //로그인 실패 시 메시지
    public String loginMessage() {
        if (this.canLogin()) return null;
        return this.status.getDescription(); // 로그인 안될 때 이유 반환
    }

    // 관리자 정보 수정
    public void updateAdmin(String name, String email, String phoneNumber){
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // 관리자 역할 변경
    public void changeRole(AdminRole role) {
        this.role = role;
    }

    // 관리자 상태 변경
    public void changeStatus(AdminStatus status) {
        this.status = status;
    }

    // 관리자 가입 승인
    public void approve() {
        this.status = AdminStatus.ACTIVE;
        approveStatus(LocalDateTime.now());
    }

    // 관리자 가입 거절
    public void reject(String rejectReason) {
        this.status = AdminStatus.REJECTED;
        this.rejectReason = rejectReason;
        rejectStatus(LocalDateTime.now());
    }
}
