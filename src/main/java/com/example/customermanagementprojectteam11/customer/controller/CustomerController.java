package com.example.customermanagementprojectteam11.customer.controller;

import com.example.customermanagementprojectteam11.customer.dto.GetCustomerResponse;
import com.example.customermanagementprojectteam11.customer.dto.PatchInfoRequest;
import com.example.customermanagementprojectteam11.customer.dto.PatchInfoResponse;
import com.example.customermanagementprojectteam11.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //고객 정보 수정
    @PatchMapping("/{id}")
    public ResponseEntity<PatchInfoResponse> patchInfo(@PathVariable Long id, @RequestBody PatchInfoRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.updateInfoCustomer(id, request));
    }

    //고객 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
    customerService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
