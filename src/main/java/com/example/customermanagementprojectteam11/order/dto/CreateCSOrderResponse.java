package com.example.customermanagementprojectteam11.order.dto;

import com.example.customermanagementprojectteam11.order.entity.OrderStatus;
import com.example.customermanagementprojectteam11.product.category.ProductCategory;
import com.example.customermanagementprojectteam11.product.status.ProductStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateCSOrderResponse {
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String productName;
    private final ProductCategory category;
    private final Long price;
    private final LocalDateTime orderDate;
    private final Long orderNumber;
    private final OrderStatus orderStatus;
    private final ProductStatus productStatus;

    public CreateCSOrderResponse(String name, String email, String phoneNumber, String productName, ProductCategory category, Long price, LocalDateTime orderDate, Long orderNumber, OrderStatus orderStatus, ProductStatus productStatus) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.orderDate = orderDate;
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.productStatus = productStatus;
    }
}
