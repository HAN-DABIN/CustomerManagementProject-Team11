package com.example.customermanagementprojectteam11.login.controller;

import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.admin.entity.AdminStatus;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminLoginController {

    private final AdminLoginService adminLoginService;



    //관리자 로그인
    @PostMapping("/admins/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request, BindingResult bindingResult, HttpSession session) { // 사용자가 보낸 이메일이랑 비번 데이터, 세션 객체

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(new LoginResponse("INVALID_INPUT", errorMessage));
        }
        Admin admin = adminLoginService.Login(request);  // 검증 통과시
        //세션 데이터 준비, 저장
        SessionAdmin sessionAdmin = new SessionAdmin(admin.getId(), admin.getEmail());
        session.setAttribute("loginAdmin", sessionAdmin);

        session.setMaxInactiveInterval(60*60*24); // 세션 유효시간 24시간 설정

        AdminStatus adminStatus = admin.getStatus(); // Enum 객체 가져오기
        String statusName = adminStatus.name();
        String description = adminStatus.getDescription(); // "활성", "승인 대기", "정지" 등

        String suffix = statusName.equals("ACTIVE") ? " 상태입니다. 로그인이 완료되었습니다." : " 상태입니다. 관리자에게 문의하세요.";
        String finalMessage = description + suffix;


        return ResponseEntity.ok(new LoginResponse(statusName, finalMessage));
    }

    // 관리자 로그아웃
    @PostMapping("/admins/logout")
    public ResponseEntity<Void> logout(
            @SessionAttribute(name = "loginAdmin", required = false) SessionAdmin sessionAdmin,
            HttpSession session) {

        // 세션에 로그인 정보가 없으면 로그아웃된 상태이거나 잘못된 접근
        if (sessionAdmin == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // 세션 무효화
        session.invalidate();

        // 성공적으로 처리
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/admin/session")
    public String session(HttpSession session) {

        Object admin = session.getAttribute("loginAdmin");

        if (admin == null) {
            return "로그인이 필요합니다";
        }

        return "관리자 접속중";
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(
            @SessionAttribute(name = "loginAdmin", required = false) SessionAdmin sessionAdmin) {

        if (sessionAdmin == null) {
            // 로그인 안 됨: 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // 로그인 됨: 200 OK
        return ResponseEntity.ok("현재 로그인된 관리자: " + sessionAdmin.getEmail());
    }


}
