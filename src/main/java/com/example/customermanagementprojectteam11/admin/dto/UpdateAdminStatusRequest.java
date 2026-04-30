package com.example.customermanagementprojectteam11.admin.dto;

import com.example.customermanagementprojectteam11.admin.entity.AdminStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateAdminStatusRequest {
    @NotNull(message = "관리자 상태를 선택해주세요.")
    private AdminStatus status;
}
