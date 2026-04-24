package com.example.customermanagementprojectteam11.admin.entity;

import lombok.Getter;


@Getter
public enum AdminStatus {
    PENDING("승인 대기"),

    private final String description;

    AdminStatus(String description) {
        this.description = description;
    }
}
