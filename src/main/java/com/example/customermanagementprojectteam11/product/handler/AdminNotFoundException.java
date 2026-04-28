package com.example.customermanagementprojectteam11.product.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class AdminNotFoundException extends AdminException {
    public AdminNotFoundException(String message){
        super(HttpStatus.NOT_FOUND, message);
    }
}
