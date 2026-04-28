package com.example.customermanagementprojectteam11.admin.dto;

import lombok.Getter;

@Getter
public class UpdateMyPasswordRequest {
        private String currentPassword;
        private String newPassword;
}
