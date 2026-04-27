package com.example.customermanagementprojectteam11.customer.controller;

import com.example.customermanagementprojectteam11.customer.dto.GetCustomerResponse;
import com.example.customermanagementprojectteam11.customer.entity.Customer;
import com.example.customermanagementprojectteam11.customer.service.CustomerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

//    //전체 조회(다건 수정 중 주석 처리)
//    @GetMapping
//    public void getAll() {
//
//    }

    //단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<GetCustomerResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getOne(id));
    }

}
