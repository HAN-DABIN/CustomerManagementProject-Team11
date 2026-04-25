package com.example.customermanagementprojectteam11.admin.service;

import com.example.customermanagementprojectteam11.admin.dto.GetAdminDetailResponse;
import com.example.customermanagementprojectteam11.admin.dto.GetAdminListResponse;
import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import com.example.customermanagementprojectteam11.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // 생성자 자동 생성
public class AdminService {
    // 속성
    public final AdminRepository adminRepository;

    // PageRequest를 사용해 페이지 정보 생성
    public Page<Admin> getAdminWithPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return adminRepository.findAll(pageable);
    }

    // 관리자 리스트 조회 기능
    @Transactional(readOnly = true)
    public GetAdminListResponse findList(String keyword, int page, int size, String sortBy, String order, AdminRole role) {
        // 페이지 기본값 1로 설정
        if (page < 1) page = 1;
        if (size < 1) size = 10 ;

        // 파라미터 잘못된 값 들어왔을 때 예외처리 (정렬기준)
        switch (sortBy) {
            case "name":
            case "email":
            case "createdAt":
                break;
            default:
                sortBy = "name";
        }
        // 파라미터 잘못된 값 들어왔을 때 예외처리 (정렬순서)
        switch (order) {
            case "asc":
            case "desc":
                break;
            default:
                order = "asc";
        }

        // 정렬 조건 생성
        Sort sort;
        // desc 입력하면 내림차순 정렬
        if(order.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
            // 그 외 값은 오름차순 정렬
        } else {
            sort = Sort.by(sortBy).ascending();
        }
        // pageable 객체 생성
        // 기본값을 1로 설정했으니 내부적 데이터는 0으로 변환하기
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        // 관리자를 List에 담아서 전체 조회하기
        Page<Admin> adminPage;
        // 키워드가 없거나 빈칸이면 전체 조회
        if (keyword == null && role == null) {
            adminPage = adminRepository.findAll(pageable);
            // 있으면 키워드 조회
        } else if (role == null) {
            adminPage = adminRepository.findByNameContainingOrEmailContaining(keyword, keyword, pageable);
        } else if (keyword == null) {
            adminPage = adminRepository.findByRole(role, pageable);
        } else {
            adminPage = adminRepository.findAll(keyword, keyword, role, pageable);
        }

        // 엔티티를 dto로 변환
        List<GetAdminListResponse.AdminDto> adminList = adminPage.getContent().stream()
                .map(admin -> new GetAdminListResponse.AdminDto(
                        admin.getId(),
                        admin.getName(),
                        admin.getEmail(),
                        admin.getPhoneNumber(),
                        admin.getRole(),
                        admin.getStatus(),
                        admin.getCreatedAt(),
                        admin.getApprovedAt()
                )).collect(Collectors.toList());
        // 응답 dto 반환
        return new GetAdminListResponse(
                page,
                size,
                adminPage.getTotalElements(),
                adminPage.getTotalPages(),
                adminList);

    }

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
                admin.getName(),
                admin.getEmail(),
                admin.getPhoneNumber(),
                admin.getRole(),
                admin.getStatus(),
                admin.getCreatedAt(),
                admin.getApprovedAt()
        );
    }

}
