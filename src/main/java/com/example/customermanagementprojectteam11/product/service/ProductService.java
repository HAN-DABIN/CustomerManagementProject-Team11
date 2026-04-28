package com.example.customermanagementprojectteam11.product.service;

import com.example.customermanagementprojectteam11.admin.entity.AdminStatus;
import com.example.customermanagementprojectteam11.product.dto.*;
import com.example.customermanagementprojectteam11.product.entity.Product;
import com.example.customermanagementprojectteam11.product.handler.AdminNotFoundException;
import com.example.customermanagementprojectteam11.product.handler.ProductNotFoundException;
import com.example.customermanagementprojectteam11.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                savedProduct.getStatus(),
                savedProduct.getCreateAt(),
                savedProduct.getUpdateAt()
        );
    }


    @Transactional(readOnly = true)
    public ProductInfoResponse getAll(GetAllProductRequest request, Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(request.getProductName(),
                request.getCategory(),
                request.getStatus(),
                pageable);
        if(productPage == null){
            throw new ProductNotFoundException("상품을 찾을 수 없습니다.");
        }
        List<GetAllProductResponse> response = productPage.getContent().stream()
                .map(product -> new GetAllProductResponse(
                        product.getProductId(),
                        product.getProductName(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getStock(),
                        product.getStatus(),
                        product.getUserName(),
                        product.getCreateAt(),
                        product.getUpdateAt()
                )).toList();
        ProductPageableResponse pageInfo = new ProductPageableResponse(
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );

        return new ProductInfoResponse(response, pageInfo);
    }

    @Transactional(readOnly = true)
    public GetOneProductResponse getOne(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("존재하지 않는 상품입니다.")
        );
        if(product.getAdmin().getStatus() != AdminStatus.ACTIVE){
            throw new AdminNotFoundException("등록된 관리자가 없습니다.");
        }

        return new GetOneProductResponse(
                product.getProductName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock(),
                product.getStatus(),
                product.getCreateAt(),
                product.getUpdateAt(),
                product.getAdmin().getName(),
                product.getAdmin().getEmail()
        );
    }

    @Transactional
    public UpdateProductResponse update(Long productId, UpdateProductRequest request) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("존재하지 않는 상품입니다.")
        );
        product.update(request.getProductName(), request.getCategory(), request.getPrice());
        return new UpdateProductResponse(
                product.getProductName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock(),
                product.getStatus(),
                product.getCreateAt(),
                product.getUpdateAt()
        );
    }

    @Transactional
    public UpdateStatusResponse updateStatus(Long productId, UpdateStatusRequest request) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("존재하지 않는 상품입니다.")
        );

        product.statusUpdate(request.getStatus());
        return new UpdateStatusResponse(
                product.getProductName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock(),
                product.getStatus(),
                product.getCreateAt(),
                product.getUpdateAt()
        );
    }

    @Transactional
    public void delete(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("존재하지 않는 일정입니다.")
        );
        productRepository.delete(product);
    }
}
