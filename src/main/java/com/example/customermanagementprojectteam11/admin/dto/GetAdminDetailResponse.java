package com.example.customermanagementprojectteam11.admin.dto;

import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import com.example.customermanagementprojectteam11.admin.entity.AdminStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetAdminDetailResponse {
    private final Long id;
    private final String email;
    private final String phoneNumber;
    private final AdminRole adminRole;
    private final AdminStatus adminStatus;
    private final LocalDateTime createdAt;
    private final LocalDateTime approvedAt;

    public GetAdminDetailResponse(Long id, String email, String phoneNumber, AdminRole adminRole, AdminStatus adminStatus, LocalDateTime createdAt, LocalDateTime approvedAt) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.adminRole = adminRole;
        this.adminStatus = adminStatus;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
    }
}
