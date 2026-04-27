package com.example.customermanagementprojectteam11.login.dto;

import lombok.Getter;

@Getter
public class SessionAdmin {

    private final Long id;
    private final String email;

    public SessionAdmin(Long id, String email {
        this.id = id;
        this.email = email;
    }

}
