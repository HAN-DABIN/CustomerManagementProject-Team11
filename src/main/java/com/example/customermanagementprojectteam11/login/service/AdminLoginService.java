package com.example.customermanagementprojectteam11.login.service;

import com.example.customermanagementprojectteam11.admin.entity.AdminStatus;
import com.example.customermanagementprojectteam11.admin.repository.AdminRepository;
import com.example.customermanagementprojectteam11.login.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminLoginService {

    private final AdminRepository adminRepository;

    @Transactional(readOnly = true)
    public Admin Login(LoginRequest request) {
        // 맨 먼저 이메일 조회
        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일이 맞지않습니다.");
        //비밀번호 조회하고
        if (!admin.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 맞지않습니다.");
        }
        // Active 상태일 때만 로그인 가능
        if (admin.getStatus() != AdminStatus.ACTIVE) {
            StatusType(admin);

        }
        return admin;

    }

    //계정 상태 종류 ( 비활성, 정지, 승인대기, 거부)
    private void statusType(Admin admin) {
        switch (admin.getStatus()) {
            case PENDING:
                throw new IllegalStateException("계정 승인 대기 중입니다.");
            case REJECTED:
                throw new IllegalStateException("계정 신청이 거부되었습니다.");
            case SUSPENDED:
                throw new IllegalStateException("계정이 정지되었습니다.");
            case INACTIVE:
                throw new IllegalStateException("비활성화된 계정입니다.");
            default:
                throw new IllegalStateException("로그인이 불가능한 상태입니다.");
        }
    }
}
