package com.example.customermanagementprojectteam11.product.service;

import com.example.customermanagementprojectteam11.product.dto.AddProductRequest;
import com.example.customermanagementprojectteam11.product.dto.AddProductResponse;
import com.example.customermanagementprojectteam11.product.entity.Product;
import com.example.customermanagementprojectteam11.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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
