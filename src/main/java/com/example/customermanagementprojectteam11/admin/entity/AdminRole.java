package com.example.customermanagementprojectteam11.admin.entity;

import lombok.Getter;

@Getter
public enum AdminRole {
    SUPER_ADMIN("\"SUPER_ADMIN\""),
    OPERATOR("\"OPERATOR\""),
    CS_ADMIN("\"CS_ADMIN\"");

    private final String description;

    AdminRole(String description) {
        this.description = description;
    }
    }

