package com.example.customermanagementprojectteam11.admin.dto;

import lombok.Getter;

@Getter
public class UpdateMyPasswordResponse {
    private final Long id;
    private final String message;

    public UpdateMyPasswordResponse(Long id, String message) {
        this.id = id;
        this.message = message;
    }
}
