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
public class CreateAdminResponse {

    private final Long id; // 상세페이지 이동하거나 로그남길때 ID값 필요
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final AdminRole role;
    private final AdminStatus status; // 승인대기 상태 명시
    private final LocalDateTime createdAt; // 등록일은 자동으로 현재시간으로 설정됨
}
