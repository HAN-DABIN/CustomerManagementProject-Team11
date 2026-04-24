package com.example.customermanageproejctproduct.controller;

import com.example.customermanageproejctproduct.dto.AddProductRequest;
import com.example.customermanageproejctproduct.dto.AddProductResponse;
import com.example.customermanageproejctproduct.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/products/add")
    public ResponseEntity<AddProductResponse> addProduct(@RequestBody AddProductRequest request, Pageable pageable){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.add(request));
    }
}
