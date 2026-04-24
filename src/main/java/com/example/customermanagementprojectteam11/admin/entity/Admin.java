package com.example.customermanagementprojectteam11.admin.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "admins")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseEntity { //BaseEntity 상속

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 200) // 유니크(중복불가) 추가
    private String email;

    @Column(nullable = false, length = 100) // 비밀번호 길이 수정 50-> 100
    private String password;

    @Column(nullable = false, length = 20)
    private String role;

    @Column(name = "phone_number",nullable = false, length = 20)
    private String phone_number;

    @Column(nullable = false,length = 20)
    private String status;

    private Admin(String name, String email,
                  String password, String role,
                   String phone_number ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone_number = phone_number;
        this.status = "승인 대기"; // 초기값 자동 설정.
    }

}
