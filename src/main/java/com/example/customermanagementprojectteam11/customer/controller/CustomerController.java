package com.example.customermanagementprojectteam11.customer.controller;

import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import com.example.customermanagementprojectteam11.admin.repository.AdminRepository;
import com.example.customermanagementprojectteam11.common.ApiResponse;
import com.example.customermanagementprojectteam11.common.exception.ForbiddenException;
import com.example.customermanagementprojectteam11.common.exception.UnauthorizedException;
import com.example.customermanagementprojectteam11.customer.dto.GetCustomerResponse;
import com.example.customermanagementprojectteam11.customer.dto.ListCustomerResponse;
import com.example.customermanagementprojectteam11.customer.dto.PatchInfoRequest;
import com.example.customermanagementprojectteam11.customer.dto.PatchInfoResponse;
import com.example.customermanagementprojectteam11.customer.service.CustomerService;
import com.example.customermanagementprojectteam11.login.dto.SessionAdmin;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final AdminRepository adminRepository;

    //전체 조회(다건 수정 중 주석 처리)
    @GetMapping
    public ResponseEntity<ApiResponse<ListCustomerResponse>> getCustomerList(
            HttpSession session,
            @RequestParam(required = false) String keyword, //없을 수 있다.
            @RequestParam(defaultValue = "1") int page, // 안 보내면 1
            @RequestParam(defaultValue = "10") int size, // 안 보내면 10
            @RequestParam(defaultValue = "createdAt") String sortBy, // 안 보내면 createdAt
            @RequestParam(defaultValue = "asc") String direction, // 안 보내면 asc 정렬
            @RequestParam(required = false) String status // 없을 수도 있다.
    ) {
        validateCustomerAuthority(session);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK,
                        "고객 목록 조회 성공",
                        customerService.getCustomerList(keyword, page, size, sortBy, direction, status)
                ));
    }

    //단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetCustomerResponse>> getOne(HttpSession session, @PathVariable Long id) {
        validateCustomerAuthority(session);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK,
                        "고객 상세 조회 성공",
                        customerService.getOne(id)
                ));
    }

    //고객 정보 수정
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<PatchInfoResponse>> patchInfo(HttpSession session, @PathVariable Long id, @RequestBody PatchInfoRequest request) {
        validateCustomerAuthority(session);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK,
                        "고객 정보 수정 성공",
                        customerService.updateInfoCustomer(id, request)
                ));
    }

    //고객 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(HttpSession session, @PathVariable Long id) {
        validateCustomerAuthority(session);
        customerService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK,
                        "고객 삭제 성공",
                        null
                ));
    }

    private void validateCustomerAuthority(HttpSession session) {
        SessionAdmin loginAdmin = (SessionAdmin) session.getAttribute("loginAdmin");

        if (loginAdmin == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        Admin admin = adminRepository.findById(loginAdmin.getId())
                .orElseThrow(() -> new UnauthorizedException("로그인 관리자 정보를 찾을 수 없습니다."));

        if (admin.getRole() != AdminRole.SUPER_ADMIN && admin.getRole() != AdminRole.CS_ADMIN) {
            throw new ForbiddenException("고객 관리 권한이 없습니다.");
        }
    }

}
