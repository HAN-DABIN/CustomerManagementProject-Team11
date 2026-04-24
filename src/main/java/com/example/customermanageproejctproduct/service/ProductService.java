package com.example.customermanageproejctproduct.service;

import com.example.customermanageproejctproduct.category.ProductCategory;
import com.example.customermanageproejctproduct.dto.AddProductRequest;
import com.example.customermanageproejctproduct.dto.AddProductResponse;
import com.example.customermanageproejctproduct.entity.Product;
import com.example.customermanageproejctproduct.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public AddProductResponse add(AddProductRequest request){
        Product product = new Product(request.getProductName(), request.getCategory(), request.getPrice(), request.getStock(), request.getStatus());

        Product savedProduct = productRepository.save(product);
        return new AddProductResponse(
                savedProduct.getUserName(),
                savedProduct.getCategory(),
                savedProduct.getPrice(),
                savedProduct.getStock(),
                savedProduct.getStatus()
        );
    }
}
