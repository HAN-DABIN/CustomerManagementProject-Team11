package com.example.customermanagementprojectteam11.product.handler;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends ProductException {
    public ProductNotFoundException(String message){
        super(HttpStatus.NOT_FOUND, message);
    }
}
