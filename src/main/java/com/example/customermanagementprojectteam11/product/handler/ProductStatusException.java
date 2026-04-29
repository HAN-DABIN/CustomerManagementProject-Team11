package com.example.customermanagementprojectteam11.product.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductStatusException extends RuntimeException{
    private HttpStatus status;

    public ProductStatusException(HttpStatus status, String message){
        super(message);
        this.status = status;
    }
}
