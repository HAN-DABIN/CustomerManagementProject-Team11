package com.example.customermanagementprojectteam11.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdateMyProfileRequest {
    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;
    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식(010-XXXX-XXXX)을 맞춰주세요.")
    private String phoneNumber;
}
