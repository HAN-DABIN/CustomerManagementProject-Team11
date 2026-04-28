package com.example.customermanagementprojectteam11.admin.controller;



import com.example.customermanagementprojectteam11.admin.dto.CreateAdminRequest;
import com.example.customermanagementprojectteam11.admin.dto.CreateAdminResponse;
import com.example.customermanagementprojectteam11.admin.dto.UpdateAdminResponse;
import com.example.customermanagementprojectteam11.admin.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/admins")
    public ResponseEntity<CreateAdminResponse> createAdmin(
           @Valid @RequestBody CreateAdminRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.save(request));
    }
    @PatchMapping("/admins/{id}")
    public ResponseEntity<UpdateAdminResponse> updateAdmin(
            @PathVariable Long id
    ){
        // 1. 서비스 레이어에 ID를 넘겨서 상태를 승인으로 변경
        UpdateAdminResponse response = adminService.approveAdmin(id);

        // 2. 변경된 결과 응답
        return ResponseEntity.ok(response);
    }


}
