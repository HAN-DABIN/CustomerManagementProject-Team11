package com.example.customermanagementprojectteam11.login.entity;

import lombok.Getter;

@Getter
public enum AdminStatus {

    ACTIVE("활성 상태"),
    INACTIVE("비활성화 상태"),
    PENDING("승인대기 상태"),
    SUSPENDED("정지 상태"),
    REJECTED("거부 상태");

    //속성
    private final String description;

    //생성자
    AdminStatus(String description) {
        this.description = description;
    }
}
