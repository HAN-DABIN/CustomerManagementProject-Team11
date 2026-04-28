package com.example.customermanagementprojectteam11.admin.dto;

import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import com.example.customermanagementprojectteam11.admin.entity.AdminStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApproveAdminResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final AdminRole adminRole;
    private final AdminStatus adminStatus;
    private final LocalDateTime approvedAt;

    public ApproveAdminResponse(Long id, String name, String email, String phoneNumber, AdminRole adminRole, AdminStatus adminStatus, LocalDateTime approvedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.adminRole = adminRole;
        this.adminStatus = adminStatus;
        this.approvedAt = approvedAt;
    }
}
