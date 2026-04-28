package com.example.customermanagementprojectteam11.admin.dto;

import com.example.customermanagementprojectteam11.admin.entity.AdminStatus;
import lombok.Getter;

@Getter
public class UpdateAdminStatusRequest {
    private AdminStatus status;
}
