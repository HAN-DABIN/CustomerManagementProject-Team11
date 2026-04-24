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

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private AdminRole role;

    @Column(name = "phone_number",nullable = false, length = 20)
    private String phone_number;

    @Enumerated(EnumType.STRING)
    private AdminStatus status;


}
