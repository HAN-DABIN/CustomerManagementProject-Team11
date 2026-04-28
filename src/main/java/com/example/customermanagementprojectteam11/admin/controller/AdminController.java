package com.example.customermanagementprojectteam11.admin.controller;

import com.example.customermanagementprojectteam11.admin.dto.*;
import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import com.example.customermanagementprojectteam11.admin.entity.AdminStatus;
import com.example.customermanagementprojectteam11.admin.service.AdminService;
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

    @PostMapping("/signup")
    public ResponseEntity<CreateAdminResponse> createAdmin(
            @Valid @RequestBody CreateAdminRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.save(request));
    }

    // 관리자 리스트 조회 API
    @GetMapping
    public ResponseEntity<GetAdminListResponse> findListAdmin(
            @RequestParam(required = false) String keyword, // 검색 키워드
            @RequestParam(defaultValue = "1") int page, // 페이지번호, 요청없으면 1페이지
            @RequestParam(defaultValue = "10") int size, // 페이지당 조회 개수, 요청없으면 기본 10개씩 조회
            @RequestParam(defaultValue = "name") String sortBy, // 정렬기준, 기본값: 이름
            @RequestParam(defaultValue = "asc") String order, // 정렬방향, 기본값: 오름차순
            @RequestParam(required = false) AdminRole role, // 역할 필터
            @RequestParam(required = false) AdminStatus status){ // 상태필터
        // 서비스에서 받은 결과 반환
        return ResponseEntity.status(HttpStatus.OK).body(adminService.findList(keyword, page, size, sortBy, order, role, status));
    }

    // 관리자 상세 조회 API
    @GetMapping("/{adminId}") // ID 값으로 관리자 상세 조회
    public ResponseEntity<GetAdminDetailResponse> findDetialAdmin(
            @PathVariable Long adminId) { // 조회할 관리자 고유 id
        // service에서 관리자 id 조회 후 200 OK 상태코드와 응답 반환
        return ResponseEntity.status(HttpStatus.OK).body(adminService.findDatail(adminId));
    }

    // 관리자 정보 수정(이름, 이메일, 전화번호) API
    @PatchMapping("/{adminId}") // ID값으로 관리자 정보 수정
    public ResponseEntity<UpdateAdminResponse> updateAdmin(
            @PathVariable Long adminId, // 수정할 관리자 Id
            @RequestBody UpdateAdminRequest request) { // 수정할 내용을 json으로 전달받아 DTO 변환
        return ResponseEntity.status(HttpStatus.OK).body(adminService.updateAdmin(request, adminId));
    }

    // 관리자 역할 변경 API
    @PatchMapping("/{adminId}/role") // ID값으로 관리자 역할 수정
    public ResponseEntity<UpdateAdminRoleResponse> updateAdminRole(
            @PathVariable Long adminId,
            @RequestBody UpdateAdminRoleRequest request) { // 역할 변경된 내용을 json으로 받아 dto 변환
        return ResponseEntity.status(HttpStatus.OK).body(adminService.updateAdminRole(request, adminId));
    }

    // 관리자 상태 변경 API
    @PatchMapping("/{adminId}/status") // ID값으로 관리자 상태 수정
    public ResponseEntity<UpdateAdminStatusResponse> updateAdminStatus(
            @PathVariable Long adminId,
            @RequestBody UpdateAdminStatusRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.updateAdminStatus(request, adminId));
    }

    // 관리자 삭제 API
    @DeleteMapping("{adminId}") // ID값으로 관리자 삭제
    public ResponseEntity<Void> deleteAdmin(
            @PathVariable Long adminId) {
        adminService.deleteAdmin(adminId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 관리자 가입 승인 API (승인대기 -> 활성)
    @PatchMapping("/{adminId}/approve") // ID값으로 관리자 상태 변경 (승인대기 -> 활성)
    public ResponseEntity<ApproveAdminResponse> approveAdmin(
            @PathVariable Long adminId) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.approveAdminStatus(adminId));

    }

    // 관리자 가입 거절 API (승인대기 -> 거절)
    @PatchMapping("/{adminId}/reject")
    public ResponseEntity<RejectAdminResponse> rejectAdmin(
            @PathVariable Long adminId,
            @RequestBody RejectAdminReasonRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.rejectAdminStatus(request, adminId));
    }


}
