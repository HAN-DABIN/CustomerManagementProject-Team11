package com.example.customermanagementprojectteam11.customer.dto;

import lombok.Getter;

@Getter
public class PatchInfoResponse {

    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String status;

    public PatchInfoResponse(String name, String email, String phoneNumber, String status) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }
}
