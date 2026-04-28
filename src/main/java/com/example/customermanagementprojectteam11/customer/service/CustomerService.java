package com.example.customermanagementprojectteam11.customer.service;

import com.example.customermanagementprojectteam11.customer.dto.GetCustomerResponse;
import com.example.customermanagementprojectteam11.customer.dto.PatchInfoRequest;
import com.example.customermanagementprojectteam11.customer.dto.PatchInfoResponse;
import com.example.customermanagementprojectteam11.customer.entity.Customer;
import com.example.customermanagementprojectteam11.customer.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class CustomerService {

    //DB에 담기 위해 데이터 타입이 CustomerRepository인 Repository 필드 생성
    private final CustomerRepository customerRepository;

    //ID 찾고 예외처리 메서드
    public Customer findByIdOrThrow(Long id) {
        return customerRepository.findById(id).orElseThrow(
                //없는 고객 조회시 404 에러 뜨게 변경
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다.")
        );
    }

    //단건 조회
    @Transactional(readOnly = true)
    public GetCustomerResponse getOne(Long id) {
        //1. id 찾고 예외처리
        Customer customer = findByIdOrThrow(id);

        //2. 찾아온 데이터 응답 DTO로 변환(내부DTO만 사용)
        GetCustomerResponse response = new GetCustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getStatus(),
                customer.getCreatedAt()
        );
        return response;
    }

    //고객 정보 수정(이름, 이메일, 전화번호)
    //변경 감지 활용
    @Transactional
    public PatchInfoResponse updateInfoCustomer(Long id, PatchInfoRequest request) {
        //1. ID 찾고 예외처리
        Customer customer = findByIdOrThrow(id);

        //2. 요청 받기, 찾은 ID 새 값으로 변경 진행
        customer.updateInfoCustomer(
                request.getName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getStatus()
        );

        //3. 응답 DTO 생성
        PatchInfoResponse response = new PatchInfoResponse(
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getStatus()
        );

        return response;
    }



//    //고객 리스트 조회(다건수정중)
//    @Transactional(readOnly = true)
//    public List<GetCustomerResponse> getAll() {
//
//        //1. 데이터 베이스에 담긴 고객 리스트 가져오기
//        //엔티티를 다 찾아와준다.
//        List<Customer> customerList = customerRepository.findAll();
//
//        //2. stream 사용하여 배열 안에 있는 모든 값에 하나한 접근 -> 지정한 방식으로 변환
//        return customerList.stream().map(customer -> new GetCustomerResponse(
//                                            //customer -> 응답 DTO로 변환
//                customer.getId(),
//                customer.getName(),
//                customer.getEmail(),
//                customer.getPhoneNumber(),
//                customer.getStatus(),
//                customer.getCreatedAt()
//        )).toList(); //리스트로 만들어주기
//
//    }



}
