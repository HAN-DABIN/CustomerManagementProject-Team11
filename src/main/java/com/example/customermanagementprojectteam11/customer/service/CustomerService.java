package com.example.customermanagementprojectteam11.customer.service;

import com.example.customermanagementprojectteam11.customer.repository.CustomerRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    //DB에 담기 위해 데이터 타입이 CustomerRepository인 Repository 필드 생성
    private final CustomerRepository customerRepository;


}
