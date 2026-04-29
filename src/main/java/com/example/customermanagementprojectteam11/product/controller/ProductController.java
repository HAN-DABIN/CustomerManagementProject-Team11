package com.example.customermanagementprojectteam11.product.controller;

import com.example.customermanagementprojectteam11.product.category.ProductCategory;
import com.example.customermanagementprojectteam11.product.dto.*;
import com.example.customermanagementprojectteam11.product.service.ProductService;
import com.example.customermanagementprojectteam11.product.status.ProductStatus;
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

    @PostMapping("/add")
    public ResponseEntity<AddProductResponse> addProduct(@Valid @RequestBody AddProductRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.add(request));
    }

    @GetMapping
    public ResponseEntity<ProductInfoResponse> getAll(
            @RequestParam(required = false) String keyword, // 상품명 검색용
            @RequestParam(required = false) ProductCategory category, // 카테고리 필터
            @RequestParam(required = false) ProductStatus status, // 상태 필터
            @RequestParam(defaultValue = "1") int page, // 페이지 번호 (기본값 1)
            @RequestParam(defaultValue = "10") int size, // 페이지당 개수 (기본값 10)
            @RequestParam(defaultValue = "createAt") String sortBy, // 정렬 기준 (기본값 생성일)
            @RequestParam(defaultValue = "desc") String direction // 정렬 순서 (기본값 내림차순)
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                productService.getAll(keyword, category, status, page, size, sortBy, direction)
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<GetOneProductResponse> getOne(@PathVariable Long productId){
        return ResponseEntity.status(HttpStatus.OK).body(productService.getOne(productId));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<UpdateProductResponse> update(@PathVariable Long productId, @Valid @RequestBody UpdateProductRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(productService.update(productId, request));
    }

    @PatchMapping("/{productId}/status")
    public ResponseEntity<UpdateStatusResponse> updateStatus(@PathVariable Long productId, @Valid @RequestBody UpdateStatusRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateStatus(productId, request));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long productId){
        productService.delete(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
