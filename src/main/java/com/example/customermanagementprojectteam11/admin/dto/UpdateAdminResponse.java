package com.example.customermanagementprojectteam11.admin.dto;

import lombok.Getter;


@Getter
public class UpdateAdminResponse {

    private final Long id;
    private final String name;

    public UpdateAdminResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
