package com.example.customermanagementprojectteam11.admin.service;

import com.example.customermanagementprojectteam11.admin.config.DuplicateEmailException;
import com.example.customermanagementprojectteam11.admin.config.PasswordEncoder;
import com.example.customermanagementprojectteam11.admin.dto.CreateAdminRequest;
import com.example.customermanagementprojectteam11.admin.dto.CreateAdminResponse;
import com.example.customermanagementprojectteam11.admin.dto.GetAdminResponse;
import com.example.customermanagementprojectteam11.admin.dto.UpdateAdminResponse;
import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.admin.entity.AdminStatus;
import com.example.customermanagementprojectteam11.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
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
    @Transactional
    public UpdateAdminResponse approveAdmin(Long id) {

        // 1. 존재하지 않는 Id일 경우 에러 던짐(404)
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 관리자가 존재하지 않습니다."));

        // 2. 이미 승인처리가 된 사람인지 확인(400)
        if (admin.getStatus() == AdminStatus.APPROVED){
            throw new IllegalStateException("이미 승인처리가 완료된 관리자 입니다.");
        }

        // 3. 상태를 승인완료로 변경
        admin.approve();

        // 4. Response dto로 변환해서 반환
        return UpdateAdminResponse.builder()
                .id(admin.getId())
                .name(admin.getName())
                .role(admin.getRole())
                .status(admin.getStatus())
                .build();

    }
    // 목록 조회
    @Transactional(readOnly = true)
    public List<GetAdminResponse> getPendingAdmins() {
        return adminRepository.findAllByStatus(AdminStatus.PENDING)
                .stream()
                .map(GetAdminResponse::from)
                .toList();
    }
}
