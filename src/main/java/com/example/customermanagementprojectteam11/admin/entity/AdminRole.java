package com.example.customermanagementprojectteam11.admin.entity;

import lombok.Getter;

@Getter
public enum AdminRole {
    SUPER_ADMIN("슈퍼관리자"),
    OPERATOR("운영관리자"),
    CS_ADMIN("CS관리자"),
    ;

    private final String description;

    AdminRole(String description) {
        this.description = description;
    }
    }

