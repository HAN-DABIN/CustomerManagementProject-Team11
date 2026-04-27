package com.example.customermanagementprojectteam11.product.controller;

import com.example.customermanagementprojectteam11.product.dto.*;
import com.example.customermanagementprojectteam11.product.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.HttpParser;
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
    public ResponseEntity<AddProductResponse> addProduct(@RequestBody AddProductRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.add(request));
    }

    @GetMapping
    public ResponseEntity<ProductInfoResponse> getAll(GetAllProductRequest request, @PageableDefault(page = 0, size = 10, sort = "price", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAll(request, pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<GetOneProductResponse> getOne(@PathVariable Long productId){
        return ResponseEntity.status(HttpStatus.OK).body(productService.getOne(productId));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<UpdateProductResponse> update(@PathVariable Long productId, @RequestBody UpdateProductRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(productService.update(productId, request));
    }

    @PatchMapping("/{productId}/status")
    public ResponseEntity<UpdateStatusResponse> updateStatus(@PathVariable Long productId, @RequestBody UpdateStatusRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateStatus(productId, request));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long productId){
        productService.delete(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
