package com.example.customermanagementprojectteam11.admin.controller;

import com.example.customermanagementprojectteam11.admin.dto.GetAdminDetailResponse;
import com.example.customermanagementprojectteam11.admin.dto.GetAdminListResponse;
import com.example.customermanagementprojectteam11.admin.service.AdminService;
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

    // 관리자 리스트 조회 API
    @GetMapping
    public ResponseEntity<GetAdminListResponse> findListAdmin(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.findList(keyword, page, size));
    }



    // 관리자 상세 조회 API
    @GetMapping("/{adminId}") // ID 값으로 관리자 상세 조회
    public ResponseEntity<GetAdminDetailResponse> findDetialAdmin(
            @PathVariable Long adminId) { // 조회할 관리자 고유 id
        // service에서 관리자 id 조회 후 200 OK 상태코드와 응답 반환
        return ResponseEntity.status(HttpStatus.OK).body(adminService.findDatail(adminId));
    }

}
