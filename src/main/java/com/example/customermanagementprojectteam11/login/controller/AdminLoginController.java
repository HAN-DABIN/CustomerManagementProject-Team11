package com.example.customermanagementprojectteam11.login.controller;

import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import com.example.customermanagementprojectteam11.admin.repository.AdminRepository;
import com.example.customermanagementprojectteam11.common.exception.ForbiddenException;
import com.example.customermanagementprojectteam11.common.exception.UnauthorizedException;
import com.example.customermanagementprojectteam11.login.dto.LoginRequest;
import com.example.customermanagementprojectteam11.login.dto.LoginResponse;
import com.example.customermanagementprojectteam11.login.dto.SessionAdmin;
import com.example.customermanagementprojectteam11.login.service.AdminLoginService;
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
    private final AdminRepository adminRepository;


    // 관리자 로그인
    @PostMapping("/admins/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            BindingResult bindingResult,
            HttpSession session) {

        // 1. 유효성 검사 (이메일 형식 등)
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(new LoginResponse("INVALID_INPUT", errorMessage));
        }

        try {
            // 2. 서비스 로직 실행 (성공 시 Admin 객체 반환, 실패 시 예외 발생)
            Admin admin = adminLoginService.Login(request);

            // 3. 세션 생성 및 데이터 저장
            SessionAdmin sessionAdmin = new SessionAdmin(admin.getId(), admin.getEmail());
            session.setAttribute("loginAdmin", sessionAdmin);
            session.setMaxInactiveInterval(60 * 60 * 24); // 24시간 유지

            // 4. 로그인 성공 메시지 구성
            String statusName = admin.getStatus().name();
            String description = admin.getStatus().getDescription();
            String finalMessage = description + " 상태입니다. 로그인이 완료되었습니다.";

            return ResponseEntity.ok(new LoginResponse(statusName, finalMessage));

        } catch (IllegalArgumentException | IllegalStateException e) {
            // 5. 로그인 싱패나 계정 상태 문제(승인 대기 등) 처리
            // 서비스에서 던진 에러 메시지를 전달 사용자한테
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("LOGIN_FAIL", e.getMessage()));
        }
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

    private void validateCustomerAuthority(HttpSession session) {
        SessionAdmin loginAdmin = (SessionAdmin) session.getAttribute("loginAdmin");

        if (loginAdmin == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        Admin admin = adminRepository.findById(loginAdmin.getId())
                .orElseThrow(() -> new UnauthorizedException("로그인 관리자 정보를 찾을 수 없습니다."));

        if (admin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new ForbiddenException("관리자 관리 권한이 없습니다.");
        }
    }
}