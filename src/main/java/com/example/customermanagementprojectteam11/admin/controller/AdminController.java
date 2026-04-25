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
            @RequestParam(required = false) String keyword, // 검색 키워드
            @RequestParam(defaultValue = "1") int page, // 페이지번호, 요청없으면 1페이지
            @RequestParam(defaultValue = "10") int size, // 페이지당 조회 개수, 요청없으면 기본 10개씩 조회
            @RequestParam(defaultValue = "name") String sortBy, // 정렬기준, 기본값: 이름
            @RequestParam(defaultValue = "asc") String order) // 정렬방향, 기본값: 오름차순
    {
        // 서비스에서 받은 결과 반환
        return ResponseEntity.status(HttpStatus.OK).body(adminService.findList(keyword, page, size, sortBy, order));
    }



    // 관리자 상세 조회 API
    @GetMapping("/{adminId}") // ID 값으로 관리자 상세 조회
    public ResponseEntity<GetAdminDetailResponse> findDetialAdmin(
            @PathVariable Long adminId) { // 조회할 관리자 고유 id
        // service에서 관리자 id 조회 후 200 OK 상태코드와 응답 반환
        return ResponseEntity.status(HttpStatus.OK).body(adminService.findDatail(adminId));
    }

}
