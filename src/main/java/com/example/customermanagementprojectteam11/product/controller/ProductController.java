package com.example.customermanagementprojectteam11.product.controller;

import com.example.customermanagementprojectteam11.product.dto.AddProductRequest;
import com.example.customermanagementprojectteam11.product.dto.AddProductResponse;
import com.example.customermanagementprojectteam11.product.dto.GetAllProductRequest;
import com.example.customermanagementprojectteam11.product.dto.ProductInfoResponse;
import com.example.customermanagementprojectteam11.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<AddProductResponse> addProduct(@RequestBody AddProductRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.add(request));
    }

    @GetMapping
    public ResponseEntity<ProductInfoResponse> getAll(GetAllProductRequest request, @PageableDefault(page = 0, size = 10, sort = "price", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAll(request, pageable));
    }
}
