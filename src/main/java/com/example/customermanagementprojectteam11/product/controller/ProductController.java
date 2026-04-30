package com.example.customermanagementprojectteam11.product.controller;

import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import com.example.customermanagementprojectteam11.admin.repository.AdminRepository;
import com.example.customermanagementprojectteam11.common.exception.ForbiddenException;
import com.example.customermanagementprojectteam11.common.exception.UnauthorizedException;
import com.example.customermanagementprojectteam11.login.dto.SessionAdmin;
import com.example.customermanagementprojectteam11.product.category.ProductCategory;
import com.example.customermanagementprojectteam11.product.dto.*;
import com.example.customermanagementprojectteam11.product.service.ProductService;
import com.example.customermanagementprojectteam11.product.status.ProductStatus;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final AdminRepository adminRepository;

    @PostMapping("/add")
    public ResponseEntity<AddProductResponse> addProduct(
            HttpSession session, // 세션
            @Valid @RequestBody AddProductRequest request){
        validateProductAuthority(session); // 인가
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.add(request));
    }

    @GetMapping
    public ResponseEntity<ProductInfoResponse> getAll(
            HttpSession session, // 세션
            @RequestParam(required = false) String keyword, // 상품명 검색용
            @RequestParam(required = false) ProductCategory category, // 카테고리 필터
            @RequestParam(required = false) ProductStatus status, // 상태 필터
            @RequestParam(defaultValue = "1") int page, // 페이지 번호 (기본값 1)
            @RequestParam(defaultValue = "10") int size, // 페이지당 개수 (기본값 10)
            @RequestParam(defaultValue = "createAt") String sortBy, // 정렬 기준 (기본값 생성일)
            @RequestParam(defaultValue = "desc") String direction // 정렬 순서 (기본값 내림차순)
    ) {
        validateProductAuthority(session); // 인가
        return ResponseEntity.status(HttpStatus.OK).body(
                productService.getAll(keyword, category, status, page, size, sortBy, direction)
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<GetOneProductResponse> getOne(
            HttpSession session, // 세션
            @PathVariable Long productId){
        validateProductAuthority(session); // 인가
        return ResponseEntity.status(HttpStatus.OK).body(productService.getOne(productId));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<UpdateProductResponse> update(
            HttpSession session, // 세션
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request){
        validateProductAuthority(session); // 인가
        return ResponseEntity.status(HttpStatus.OK).body(productService.update(productId, request));
    }

    @PatchMapping("/{productId}/status")
    public ResponseEntity<UpdateStatusResponse> updateStatus(
            HttpSession session, // 세션
            @PathVariable Long productId,
            @Valid @RequestBody UpdateStatusRequest request){
        validateProductAuthority(session); // 인가
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateStatus(productId, request));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(
            HttpSession session, // 세션
            @PathVariable Long productId){
        validateProductAuthority(session); // 인가
        productService.delete(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 상품 관리 권한 확인 (SUPER_ADMIN 또는 CS_ADMIN)
    private void validateProductAuthority(HttpSession session) {
        SessionAdmin loginAdmin = (SessionAdmin) session.getAttribute("loginAdmin");

        if (loginAdmin == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        Admin admin = adminRepository.findById(loginAdmin.getId())
                .orElseThrow(() -> new UnauthorizedException("로그인 관리자 정보를 찾을 수 없습니다."));

        if (admin.getRole() != AdminRole.SUPER_ADMIN && admin.getRole() != AdminRole.CS_ADMIN) {
            throw new ForbiddenException("상품 관리 권한이 없습니다.");
        }
    }
}
