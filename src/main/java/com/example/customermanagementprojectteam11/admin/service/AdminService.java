package com.example.customermanagementprojectteam11.admin.service;

import com.example.customermanagementprojectteam11.admin.config.DuplicateEmailException;
import com.example.customermanagementprojectteam11.admin.config.PasswordEncoder;
import com.example.customermanagementprojectteam11.admin.dto.*;
import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import com.example.customermanagementprojectteam11.admin.entity.AdminStatus;
import com.example.customermanagementprojectteam11.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // 생성자 자동 생성
public class AdminService {
    // 속성
    public final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public CreateAdminResponse save(CreateAdminRequest request){

        // 1. 이메일 중복 체크 [중복시 에러반환]
        if (adminRepository.existsByEmail(request.getEmail())){
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
        }

        // 2. 비밀번호 암호화 [BCrypt 사용]
        String encodedPassword = passwordEncoder.encode(request.getPassword());


        // 3. 엔티티 생성(데이터 저장목적 request 단계 + 암호화된 비번 저장)
        Admin admin = new Admin(
                request.getName(),
                request.getEmail(),
                encodedPassword, // 암호화된 비번으로 저장
                request.getRole(),
                request.getPhoneNumber()
        );
        // 4. DB 레포지토리에 저장
        Admin savedAdmin = adminRepository.save(admin);

        // 5. Response DTO로 변환 (빌더패턴/ 데이터 전달목적 결과 보여주는 response 단계)
        return CreateAdminResponse.builder()
                .id(savedAdmin.getId())
                .name(savedAdmin.getName())
                .email(savedAdmin.getEmail())
                .phoneNumber(savedAdmin.getPhoneNumber())
                .role(savedAdmin.getRole())
                .status(savedAdmin.getStatus())
                .createdAt(savedAdmin.getCreatedAt())
                .build();

    }

    // 관리자 리스트 조회 기능
    @Transactional(readOnly = true)
    public GetAdminListResponse findList(
            String keyword,
            int page,
            int size,
            String sortBy,
            String order,
            AdminRole role,
            AdminStatus status) {
        /**
         * 1. 잘못된 page/size 보정
         * 2. 잘못된 sortBy/order 보정
         * 3. 정렬 객체 생성
         * 4. Pageable 생성
         * 5. 검색 조건(specification) 생성
         * 6. keyword / role / status 조건 추가
         * 7. DB 조회
         */
        // 페이지 기본값 1로 설정
        if (page < 1) page = 1;
        if (size < 1) size = 10;

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

        // desc 요청하면 내림차순 정렬
        if (order.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
            // 그 외 값은 오름차순 정렬
        } else {
            sort = Sort.by(sortBy).ascending();
        }
        // pageable 객체 생성
        // 기본값을 1로 설정했으니 내부적 데이터는 0으로 변환하기
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        // Specification 생성 - 빈 조건(전체조회)부터 시작
        Specification<Admin> spec = Specification.allOf();

        // 검색키워드가 null이 아니고 비어있지 않을 때 검색조건 추가
        if (keyword != null && !keyword.isBlank()) {
            spec = spec.and(AdminSpecification.keyword(keyword));
        }
        // 역할이 null이 아닐 때 역할필터 추가
        if (role != null) {
            spec = spec.and(AdminSpecification.role(role));
        }
        // 상태가 null이 아닐 때 상태필터 추가
        if (status != null) {
            spec = spec.and(AdminSpecification.status(status));
        }

        // 조회
        Page<Admin> adminPage = adminRepository.findAll(spec, pageable);

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

    // 관리자 정보 수정 기능
    @Transactional
    public UpdateAdminResponse updateAdmin(UpdateAdminRequest request, Long adminId) {
        // adminId로 관리자 조회
        Admin admin = adminRepository.findById(adminId)
                // 없으면 예외 발생
                .orElseThrow(() -> new IllegalStateException("해당 관리자가 없습니다."));
        // entity 값 변경
        admin.updateAdmin(
                request.getName(),
                request.getEmail(),
                request.getPhoneNumber()
        );
        // 응답 dto 반환
        return new UpdateAdminResponse(
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getPhoneNumber(),
                admin.getModifiedAt()
        );
    }

    // 관리자 역할 변경 기능
    @Transactional
    public UpdateAdminRoleResponse updateAdminRole(UpdateAdminRoleRequest request, Long adminId) {
        // adminId로 관리자 조회
        Admin admin = adminRepository.findById(adminId)
                // 없으면 예외 발생
                .orElseThrow(() -> new IllegalStateException("해당 관리자가 없습니다."));
        // 엔티티 값 요청받은 값으로 변경
        admin.changeRole(
                request.getRole()
        );
        // 응답 dto 반환
        return new UpdateAdminRoleResponse(
                admin.getId(),
                admin.getName(),
                admin.getRole(),
                admin.getModifiedAt()
        );
    }

    // 관리자 상태 변경 기능
    @Transactional
    public UpdateAdminStatusResponse updateAdminStatus(UpdateAdminStatusRequest request, Long adminId) {
        // adminId로 관리자 조회
        Admin admin = adminRepository.findById(adminId)
                // 없으면 예외 발생
                .orElseThrow(() -> new IllegalStateException("해당 관리자가 없습니다."));
        // 엔티티에 상태 변경 요청
        admin.changeStatus(
                request.getStatus()
        );
        // 응답 dto 반환
        return new UpdateAdminStatusResponse(
                admin.getId(),
                admin.getName(),
                admin.getStatus(),
                admin.getModifiedAt()
        );
    }

    // 관리자 삭제 기능 (soft delete)
    @Transactional
    public void deleteAdmin(Long id) {
        // adminId로 관리자 조회
        Admin admin = adminRepository.findById(id)
                // 없으면 예외처리
                .orElseThrow(() -> new IllegalStateException("유저를 찾을 수 없습니다"));
        // 관리자 삭제 -> is_delete에 업데이트 댐
        adminRepository.delete(admin);
    }

    // 관리자 가입 승인 기능
    @Transactional
    public ApproveAdminResponse approveAdminStatus(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                // 없으면 예외 발생
                .orElseThrow(() -> new IllegalStateException("해당 관리자가 없습니다."));
        // 관리자의 상태가 승인대기 상태가 아니라면
        if (admin.getStatus() != AdminStatus.PENDING) {
            // 예외 발생
            throw new IllegalStateException("승인대기 상태에서만 승인이 가능합니다.");
        }
        // 엔티티에서 승인상태 요청하기
        admin.approve();
        // 응답 dto 반환
        return new ApproveAdminResponse(
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getPhoneNumber(),
                admin.getRole(),
                admin.getStatus(),
                admin.getApprovedAt()
        );
    }

    // 관리자 승인 거부 기능
    @Transactional
    public RejectAdminResponse rejectAdminStatus(RejectAdminReasonRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                // 없으면 예외 발생
                .orElseThrow(() -> new IllegalStateException("해당 관리자가 없습니다."));
        // 관리자의 상태가 승인대기 상태가 아니라면
        if (admin.getStatus() != AdminStatus.PENDING) {
            // 예외 발생
            throw new IllegalStateException("승인대기 상태에서만 승인거부가 가능합니다.");
        }
        // 엔티티에서 승인거부 및 거절 사유 업데이트 요청하기
        admin.reject(
                request.getRejectReason()
        );
        // 응답 dto 반환
        return new RejectAdminResponse(
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getPhoneNumber(),
                admin.getRole(),
                admin.getStatus(),
                admin.getRejectReason(),
                admin.getRejectedAt()
        );
    }

    // 내 프로필 조회 기능
    @Transactional(readOnly = true)
    public GetMyProfileResponse getMyProfile(Long adminId) {
        // 엔티티에서 로그인된 아이디 찾기
        Admin admin = adminRepository.findById(adminId)
                // 없으면 예외 발생
                .orElseThrow(() ->
                        new IllegalStateException("관리자 정보를 찾을 수 없습니다."));
        return new GetMyProfileResponse(
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getPhoneNumber()
        );
    }

    // 내 프로필 수정 기능
    @Transactional
    public UpdateMyProfileResponse updateMyProfile(UpdateMyProfileRequest request, Long adminId) {
        // adminId로 관리자 조회
        Admin admin = adminRepository.findById(adminId)
                // 없으면 예외 발생
                .orElseThrow(() -> new IllegalStateException("해당 관리자가 없습니다."));
        // entity 값 변경
        admin.updateMyProfile(
                request.getName(),
                request.getEmail(),
                request.getPhoneNumber()
        );
        // 응답 dto 반환
        return new UpdateMyProfileResponse(
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getPhoneNumber(),
                admin.getModifiedAt()
        );
    }

    // 비밀번호 변경 기능
    @Transactional
    public UpdateMyPasswordResponse updateMyPassword(UpdateMyPasswordRequest request, Long adminId) {
        // adminId로 관리자 조회
        Admin admin = adminRepository.findById(adminId)
                // 없으면 예외 발생
                .orElseThrow(() -> new IllegalStateException("해당 관리자가 없습니다."));
        // 현재 비밀번호 검증
        // 저장되어있는 비밀번호가 요청바디의 비밀번호와 다르다면 예외 발생
        if (!passwordEncoder.matches(request.getCurrentPassword(),admin.getPassword())) {
            throw new RuntimeException("현재 비밀번호가 올바르지 않습니다.");
        }
        // 새 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());

        // 엔티티에 변경됨 비밀번호 저장
        admin.updateMyPassword(encodedPassword);

        // 응답 dto 반환
        return new UpdateMyPasswordResponse(
                admin.getId(),
                "비밀번호가 변경되었습니다."
        );
    }
}
