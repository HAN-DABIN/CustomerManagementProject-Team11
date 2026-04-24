package com.example.customermanageproejctproduct.service;

import com.example.customermanageproejctproduct.category.ProductCategory;
import com.example.customermanageproejctproduct.dto.AddProductRequest;
import com.example.customermanageproejctproduct.dto.AddProductResponse;
import com.example.customermanageproejctproduct.dto.GetAllProductRequest;
import com.example.customermanageproejctproduct.dto.GetAllProductResponse;
import com.example.customermanageproejctproduct.entity.Product;
import com.example.customermanageproejctproduct.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public AddProductResponse add(AddProductRequest request){
        Product product = new Product(request.getProductName(), request.getCategory(), request.getPrice(), request.getStock(), request.getStatus());

        Product savedProduct = productRepository.save(product);
        return new AddProductResponse(
                savedProduct.getProductName(),
                savedProduct.getCategory(),
                savedProduct.getPrice(),
                savedProduct.getStock(),
                savedProduct.getStatus()
        );
    }


    @Transactional
    public Page<GetAllProductResponse> getAll(GetAllProductRequest request, Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(request.getProductName(),
                request.getCategory(),
                request.getStatus(),
                pageable);
        return productPage.map(
                page -> new GetAllProductResponse(
                        page.getProductId(),
                        page.getProductName(),
                        page.getCategory(),
                        page.getPrice(),
                        page.getStock(),
                        page.getStatus(),
                        page.getUserName()
                )
        );
    }
}
