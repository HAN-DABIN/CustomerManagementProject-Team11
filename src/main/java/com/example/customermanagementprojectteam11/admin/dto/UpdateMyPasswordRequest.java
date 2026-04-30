package com.example.customermanagementprojectteam11.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateMyPasswordRequest {
        @NotBlank(message = "현재 비밀번호를 입력해 주십시오.")
        private String currentPassword;
        @NotBlank(message = "새로운 비밀번호를 입력해 주십시오.")
        private String newPassword;
}
