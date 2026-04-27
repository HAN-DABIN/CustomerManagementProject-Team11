package com.example.customermanagementprojectteam11.admin.dto;

import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import com.example.customermanagementprojectteam11.admin.entity.AdminStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetAdminListResponse {
    private int currentPage;
    private int pageSize;
    private long totalCount;
    private int totalPages;
    private List<AdminDto> adminDtoList;

    // 내부 DTO
    @Getter
    @AllArgsConstructor
    public static class AdminDto {
        private final Long id;
        private final String name;
        private final String email;
        private final String phoneNumber;
        private final AdminRole adminRole;
        private final AdminStatus adminStatus;
        private final LocalDateTime createdAt;
        private final LocalDateTime approvedAt;

    }

}
