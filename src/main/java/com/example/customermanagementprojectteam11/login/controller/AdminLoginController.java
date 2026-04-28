package com.example.customermanagementprojectteam11.login.controller;

import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.login.dto.LoginRequest;
import com.example.customermanagementprojectteam11.login.dto.LoginResponse;
import com.example.customermanagementprojectteam11.login.dto.SessionAdmin;
import com.example.customermanagementprojectteam11.login.service.AdminLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminLoginController {

    private final AdminLoginService adminLoginService;

    //관리자 로그인
    @PostMapping("/admins/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request, HttpSession session) { // 사용자가 보낸 이메일이랑 비번 데이터, 세션 객체
        Admin admin = adminLoginService.Login(request);
        SessionAdmin sessionAdmin = new SessionAdmin(admin.getId(), admin.getEmail());
        session.setAttribute("loginAdmin", sessionAdmin);   // 세션에 로그인 정보보관
        session.setMaxInactiveInterval(60*60*24); // 세션 유효시간 24시간 설정
        LoginResponse response = new LoginResponse(admin.getId(), admin.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 관리자 로그아웃
    @PostMapping("/admins/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // 세션 가져오기 (없으면 null)
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();  //세션 무효
        }

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @GetMapping("/admin/session")
    public String session(HttpSession session) {

        Object admin = session.getAttribute("loginAdmin");

        if (admin == null) {
            return "로그인이 필요합니다";
        }

        return "관리자 접속중";
    }
}
