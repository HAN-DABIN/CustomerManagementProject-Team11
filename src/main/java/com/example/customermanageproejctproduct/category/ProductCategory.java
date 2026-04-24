package com.example.customermanageproejctproduct.category;

import lombok.Getter;

@Getter
public enum ProductCategory {
    ELECTRONIC("전자기기"),
    CLOTHES("의류"),
    FOOD("식품");

    private final String foodStatus;

    ProductCategory(String status){
        this.foodStatus = status;
    }
}
