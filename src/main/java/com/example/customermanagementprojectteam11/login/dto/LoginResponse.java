package com.example.customermanagementprojectteam11.login.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String status;
    private String message;

    public LoginResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
