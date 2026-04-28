package com.example.customermanagementprojectteam11.admin.dto;

import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import com.example.customermanagementprojectteam11.admin.entity.AdminStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UpdateAdminResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final AdminRole role;
    private final AdminStatus status;
    private final LocalDateTime createdAt;


}
