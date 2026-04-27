package com.example.customermanagementprojectteam11.admin.dto;

import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateAdminRoleResponse {
    private final Long id;
    private final String name;
    private final AdminRole role;
    private final LocalDateTime modifiedAt;

    public UpdateAdminRoleResponse(long id, String name, AdminRole role, LocalDateTime modifiedAt) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.modifiedAt = modifiedAt;
    }
}
