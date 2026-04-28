package com.example.customermanagementprojectteam11.customermanageproejctproduct.service;

import com.example.customermanagementprojectteam11.customermanageproejctproduct.dto.AddProductRequest;
import com.example.customermanagementprojectteam11.customermanageproejctproduct.dto.AddProductResponse;
import com.example.customermanagementprojectteam11.customermanageproejctproduct.dto.GetAllProductRequest;
import com.example.customermanagementprojectteam11.customermanageproejctproduct.dto.GetAllProductResponse;
import com.example.customermanagementprojectteam11.customermanageproejctproduct.entity.Product;
import com.example.customermanagementprojectteam11.customermanageproejctproduct.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
