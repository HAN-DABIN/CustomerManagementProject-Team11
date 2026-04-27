package com.example.customermanagementprojectteam11.admin.dto;

import com.example.customermanagementprojectteam11.admin.entity.AdminStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateAdminStatusResponse {
    private final Long id;
    private final String name;
    private final AdminStatus status;
    private final LocalDateTime modifiedAt;

    public UpdateAdminStatusResponse(Long id, String name, AdminStatus status, LocalDateTime modifiedAt) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.modifiedAt = modifiedAt;
    }
}
