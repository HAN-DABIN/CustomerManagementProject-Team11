package com.example.customermanagementprojectteam11.admin.dto;

import lombok.Getter;

@Getter
public class GetMyProfileResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final String phoneNumber;

    public GetMyProfileResponse(Long id, String name, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
