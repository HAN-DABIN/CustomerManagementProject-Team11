package com.example.customermanagementprojectteam11.product.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AdminException extends RuntimeException {
    private final HttpStatus status;

    public AdminException(HttpStatus status, String message){
        super(message);
        this.status = status;
    }
}
