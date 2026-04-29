package com.example.customermanagementprojectteam11.admin.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateMyProfileResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final LocalDateTime modifiedAt;

    public UpdateMyProfileResponse(Long id, String name, String email, String phoneNumber, LocalDateTime modifiedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.modifiedAt = modifiedAt;
    }
}
