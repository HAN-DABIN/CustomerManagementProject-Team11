package com.example.customermanagementprojectteam11.admin.controller;

import com.example.customermanagementprojectteam11.admin.dto.*;
import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import com.example.customermanagementprojectteam11.admin.entity.AdminStatus;
import com.example.customermanagementprojectteam11.admin.repository.AdminRepository;
import com.example.customermanagementprojectteam11.common.exception.ForbiddenException;
import com.example.customermanagementprojectteam11.common.exception.UnauthorizedException;
import com.example.customermanagementprojectteam11.admin.service.AdminService;
import com.example.customermanagementprojectteam11.common.ApiResponse;
import com.example.customermanagementprojectteam11.login.dto.SessionAdmin;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor // 생성자 자동 생성
public class AdminController {
    // 속성
    public final AdminService adminService;
    private final AdminRepository adminRepository;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<CreateAdminResponse>> createAdmin(
            @Valid @RequestBody CreateAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED,
                        "관리자 회원가입 성공",
                        adminService.save(request)));
    }

    // 관리자 리스트 조회 API
    @GetMapping
    public ResponseEntity<ApiResponse<GetAdminListResponse>> findListAdmin(
            HttpSession session, // 세션 추가
            @RequestParam(required = false) String keyword, // 검색 키워드
            @RequestParam(defaultValue = "1") int page, // 페이지번호, 요청없으면 1페이지
            @RequestParam(defaultValue = "10") int size, // 페이지당 조회 개수, 요청없으면 기본 10개씩 조회
            @RequestParam(defaultValue = "name") String sortBy, // 정렬기준, 기본값: 이름
            @RequestParam(defaultValue = "asc") String order, // 정렬방향, 기본값: 오름차순
            @RequestParam(required = false) AdminRole role, // 역할 필터
            @RequestParam(required = false) AdminStatus status) { // 상태필터

        validateCustomerAuthority(session); // 인가
        // 서비스에서 받은 결과 반환
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK,
                        "관리자 리스트 조회 성공",
                        adminService.findList(keyword, page, size, sortBy, order, role, status)));
    }

    // 관리자 상세 조회 API
    @GetMapping("/{adminId}") // ID 값으로 관리자 상세 조회
    public ResponseEntity<ApiResponse<GetAdminDetailResponse>> findDetialAdmin(
            HttpSession session, // 세션 추가
            @PathVariable Long adminId) { // 조회할 관리자 고유 id
        validateCustomerAuthority(session); // 인가
        // service에서 관리자 id 조회 후 200 OK 상태코드와 응답 반환
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK,
                        "관리자 상세 조회 성공",
                        adminService.findDatail(adminId)));
    }

    // 관리자 정보 수정(이름, 이메일, 전화번호) API
    @PatchMapping("/{adminId}") // ID값으로 관리자 정보 수정
    public ResponseEntity<ApiResponse<UpdateAdminResponse>> updateAdmin(
            HttpSession session, // 세션 추가
            @PathVariable Long adminId, // 수정할 관리자 Id
            @Valid @RequestBody UpdateAdminRequest request) { // 수정할 내용을 json으로 전달받아 DTO 변환

        validateCustomerAuthority(session); // 인가

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK,
                        "관리자 정보 수정 성공",
                        adminService.updateAdmin(request, adminId)));
    }

    // 관리자 역할 변경 API
    @PatchMapping("/{adminId}/role") // ID값으로 관리자 역할 수정
    public ResponseEntity<ApiResponse<UpdateAdminRoleResponse>> updateAdminRole(
            HttpSession session, // 세션
            @PathVariable Long adminId,
            @Valid @RequestBody UpdateAdminRoleRequest request) { // 역할 변경된 내용을 json으로 받아 dto 변환

        validateCustomerAuthority(session); // 인가

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK,
                        "관리자 역할 변경 성공",
                        adminService.updateAdminRole(request, adminId)));
    }

    // 관리자 상태 변경 API
    @PatchMapping("/{adminId}/status") // ID값으로 관리자 상태 수정
    public ResponseEntity<ApiResponse<UpdateAdminStatusResponse>> updateAdminStatus(
            HttpSession session, // 세션 추가
            @PathVariable Long adminId,
            @Valid @RequestBody UpdateAdminStatusRequest request) {

        validateCustomerAuthority(session); // 인가

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK,
                        "관리자 상태 변경 성공",
                        adminService.updateAdminStatus(request, adminId)));
    }

    // 관리자 삭제 API
    @DeleteMapping("{adminId}") // ID값으로 관리자 삭제
    public ResponseEntity<Void> deleteAdmin(
            HttpSession session, // 세션 추가
            @PathVariable Long adminId) {

        validateCustomerAuthority(session); // 인가

        adminService.deleteAdmin(adminId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 관리자 가입 승인 API (승인대기 -> 활성)
    @PatchMapping("/{adminId}/approve") // ID값으로 관리자 상태 변경 (승인대기 -> 활성)
    public ResponseEntity<ApiResponse<ApproveAdminResponse>> approveAdmin(
            HttpSession session, // 세션 추가
            @PathVariable Long adminId) {

        validateCustomerAuthority(session); // 인가

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK,
                        "관리자 가입 승인 성공",
                        adminService.approveAdminStatus(adminId)));

    }

    // 관리자 가입 거절 API (승인대기 -> 거절)
    @PatchMapping("/{adminId}/reject")
    public ResponseEntity<ApiResponse<RejectAdminResponse>> rejectAdmin(
            HttpSession session, // 세션
            @PathVariable Long adminId,
            @Valid @RequestBody RejectAdminReasonRequest request) {

        validateCustomerAuthority(session); // 인가

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK,
                        "관리자 가입 거절 성공",
                        adminService.rejectAdminStatus(request, adminId)));
    }

    // 내 프로필 조회 API
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<GetMyProfileResponse>> getMyProfile(
            HttpSession session) { // 로그인 세션 정보 받기
        // 세션에 저장된 로그인 관리자 정보 꺼내고
        SessionAdmin loginAdmin = (SessionAdmin) session.getAttribute("loginAdmin");
        // 만약 로그인이 안 된 상태면 401 반환하기
        if (loginAdmin == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
        // 로그인 한 관리자 id를 서비스로 전달해서 프로필 조회
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK,
                        "내 프로필 조회 성공",
                        adminService.getMyProfile(loginAdmin.getId())));
    }

    // 내 프로필 수정 API
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UpdateMyProfileResponse>> updateMyProfile(
            HttpSession session, // 로그인 세션 정보 받기
            @Valid @RequestBody UpdateMyProfileRequest request) { // 수정 바디 받기
        // 세션에 저장된 로그인 관리자 정보 꺼내고
        SessionAdmin loginAdmin = (SessionAdmin) session.getAttribute("loginAdmin");
        // 만약 로그인이 안 된 상태면 401 반환하기
        if (loginAdmin == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK,
                        "내 프로필 수정 성공",
                        adminService.updateMyProfile(request, loginAdmin.getId())));
    }

    // 비밀번호 변경 API
    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<UpdateMyPasswordResponse>> updateMyPassword(
            HttpSession session, // 로그인 세션 정보 받기
            @Valid
            @RequestBody UpdateMyPasswordRequest request) { // 비밀번호 변경 바디 받기
        // 세션에 저장된 로그인 관리자 정보 꺼내고
        SessionAdmin loginAdmin = (SessionAdmin) session.getAttribute("loginAdmin");
        // 만약 로그인이 안 된 상태면 401 반환하기
        if (loginAdmin == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK,
                        "비밀번호 변경 성공",
                        adminService.updateMyPassword(request, loginAdmin.getId())));
    }

    // 인가 로직 메서드
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

