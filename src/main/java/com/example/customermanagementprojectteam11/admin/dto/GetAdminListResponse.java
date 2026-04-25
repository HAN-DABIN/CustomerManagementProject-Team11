package com.example.customermanagementprojectteam11.admin.dto;

import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import com.example.customermanagementprojectteam11.admin.entity.AdminStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetAdminListResponse {
    private List<AdminDto> adminDtoList;

    public GetAdminListResponse(List<AdminDto> adminDtoList) {
        this.adminDtoList = adminDtoList;
    }

    public List<AdminDto> getAdminDtoList() {
        return adminDtoList;
    }

    // 내부 DTO
    public static class AdminDto {
        private final Long id;
        private final String name;
        private final String email;
        private final String phoneNumber;
        private final AdminRole adminRole;
        private final AdminStatus adminStatus;
        private final LocalDateTime createdAt;
        private final LocalDateTime approvedAt;


        public AdminDto(Long id, String name, String email, String phoneNumber, AdminRole adminRole, AdminStatus adminStatus, LocalDateTime createdAt, LocalDateTime approvedAt) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.adminRole = adminRole;
            this.adminStatus = adminStatus;
            this.createdAt = createdAt;
            this.approvedAt = approvedAt;
        }
    }

}
