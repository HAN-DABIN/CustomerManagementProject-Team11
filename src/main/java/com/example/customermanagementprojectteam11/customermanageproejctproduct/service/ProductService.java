package com.example.customermanagementprojectteam11.customermanageproejctproduct.service;

import com.example.customermanagementprojectteam11.customermanageproejctproduct.dto.*;
import com.example.customermanagementprojectteam11.customermanageproejctproduct.entity.Product;
import com.example.customermanagementprojectteam11.customermanageproejctproduct.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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


    @Transactional(readOnly = true)
    public ProductInfoResponse getAll(GetAllProductRequest request, Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(request.getProductName(),
                request.getCategory(),
                request.getStatus(),
                pageable);
        List<GetAllProductResponse> response = productPage.getContent().stream()
                .map(product -> new GetAllProductResponse(
                        product.getProductId(),
                        product.getProductName(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getStock(),
                        product.getStatus(),
                        product.getUserName()
                )).toList();
        ProductPageableResponse pageInfo = new ProductPageableResponse(
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );

        return new ProductInfoResponse(response, pageInfo);
    }
}
