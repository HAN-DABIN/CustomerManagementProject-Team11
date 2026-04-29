package com.example.customermanagementprojectteam11.product.handler;


import org.springframework.http.HttpStatus;

public class ProductStatusErrorException extends ProductStatusException{
    public ProductStatusErrorException(String message){
        super(HttpStatus.BAD_REQUEST, message);
    }

}
