package com.example.customermanagementprojectteam11.admin.dto;

import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateAdminRoleRequest {
    @NotNull(message = "관리자 역할을 선택해주세요.")
    private AdminRole role;
}
