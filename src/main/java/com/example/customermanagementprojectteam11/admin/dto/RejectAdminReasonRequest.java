package com.example.customermanagementprojectteam11.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RejectAdminReasonRequest {
    @NotBlank(message = "가입 거절 사유는 필수 입력입니다.")
    private String rejectReason;
}
