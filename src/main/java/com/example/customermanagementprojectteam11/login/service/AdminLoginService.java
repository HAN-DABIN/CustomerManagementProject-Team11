package com.example.customermanagementprojectteam11.login.service;

import com.example.customermanagementprojectteam11.admin.config.PasswordEncoder;
import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.admin.repository.AdminRepository;
import com.example.customermanagementprojectteam11.login.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminLoginService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Admin Login(LoginRequest request) {
        // 먼저 이메일 조회
        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일이 맞지않습니다."));
        //비밀번호 조회하고 (암호화 된 비밀번호랑 입력된 비밀번호 검증)
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 맞지않습니다.");
        }
        // Active 상태일 때만 로그인 가능
        if (!admin.canLogin()) {
            throw new IllegalStateException(admin.loginMessage());
        }

        //session.setAttribute("loginAdmin", admin); // 세션 인증
        return admin;

    }

}
