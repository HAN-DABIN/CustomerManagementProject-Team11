package com.example.customermanagementprojectteam11.admin.dto;

import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import com.example.customermanagementprojectteam11.admin.entity.AdminStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateAdminResponse {

    private final Long id;
    private final String name;
    private final AdminRole role;
    private final AdminStatus status;

}
