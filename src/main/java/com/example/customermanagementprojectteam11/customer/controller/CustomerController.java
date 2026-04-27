package com.example.customermanagementprojectteam11.customer.controller;

import com.example.customermanagementprojectteam11.customer.entity.Customer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Customer")
@RequiredArgsConstructor
public class CustomerController {

    //전체 조회
    @GetMapping
    public void getAll() {

    }
}
