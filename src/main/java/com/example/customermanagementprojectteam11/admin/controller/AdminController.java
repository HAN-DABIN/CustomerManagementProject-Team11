package com.example.customermanagementprojectteam11.admin.controller;



import com.example.customermanagementprojectteam11.admin.dto.CreateAdminRequest;
import com.example.customermanagementprojectteam11.admin.dto.CreateAdminResponse;
import com.example.customermanagementprojectteam11.admin.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/admins")
    public ResponseEntity<CreateAdminResponse> createAdmin(
           @Valid @RequestBody CreateAdminRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.save(request));
    }


}
