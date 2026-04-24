package com.example.customermanagementprojectteam11.admin.entity;

import lombok.Getter;


@Getter
public enum AdminStatus {
    PENDING("승인 대기"),
    APPROVED("승인 완료"),
    REJECTED("거절됨");

    private final String description;

    AdminStatus(String description) {
        this.description = description;
    }
}
