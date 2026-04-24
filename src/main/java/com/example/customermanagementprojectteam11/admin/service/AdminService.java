package com.example.customermanagementprojectteam11.admin.service;

import com.example.customermanagementprojectteam11.admin.dto.GetAdminDetailResponse;
import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // 생성자 자동 생성
public class AdminService {
    // 속성
    public final AdminRepository adminRepository;

    // 관리자 상세 조회 기능
    @Transactional(readOnly = true) // 읽기 전용
    public GetAdminDetailResponse findDatail(Long adminId) {
        // adminId로 관리자 조회
        Admin admin = adminRepository.findById(adminId)
                // 없으면 예외 발생
                .orElseThrow(() -> new IllegalStateException("해당 관리자가 없습니다."));
        // 조회 성공 시 dto 반환
        return new GetAdminDetailResponse(
                admin.getId(),
                admin.getEmail(),
                admin.getPhoneNumber(),
                admin.getRole(),
                admin.getStatus(),
                admin.getCreatedAt(),
                admin.getApprovedAt()
        );
    }
}
