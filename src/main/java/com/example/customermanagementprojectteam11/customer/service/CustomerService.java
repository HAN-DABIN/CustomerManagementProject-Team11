package com.example.customermanagementprojectteam11.customer.service;

import com.example.customermanagementprojectteam11.common.exception.NotFoundException;
import com.example.customermanagementprojectteam11.customer.dto.GetCustomerResponse;
import com.example.customermanagementprojectteam11.customer.dto.ListCustomerResponse;
import com.example.customermanagementprojectteam11.customer.dto.PatchInfoRequest;
import com.example.customermanagementprojectteam11.customer.dto.PatchInfoResponse;
import com.example.customermanagementprojectteam11.customer.entity.Customer;
import com.example.customermanagementprojectteam11.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomerService {

    //DB에 담기 위해 데이터 타입이 CustomerRepository인 Repository 필드 생성
    private final CustomerRepository customerRepository;

    //ID 찾고 예외처리 메서드
    public Customer findByIdOrThrow(Long id) {
        return customerRepository.findById(id).orElseThrow(
                //없는 고객 조회시 404 에러 뜨게 변경
                () -> new NotFoundException("해당 사용자를 찾을 수 없습니다.")
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

        //응답 DTO 반환
        return response;
    }

    //고객 삭제
    @Transactional
    public void delete(Long id) {
        //1. 삭제할 데이터 찾기, 없으면 예외처리
        Customer customer = findByIdOrThrow(id);

        //2. 삭제하기
        customerRepository.deleteById(id);
    }

    // 고객 리스트 조회
    @Transactional(readOnly = true)
    public ListCustomerResponse getCustomerList(
            // 1. 입력값 받기
            // 컨트롤러가 @RequestParam으로 받은 값을 서비스로 넘겨준 것
            String keyword,   // 검색 키워드 (이름 또는 이메일)
            int page,         // 페이지 번호 (사용자 기준 1페이지부터 시작)
            int size,         // 페이지당 개수
            String sortBy,    // 정렬 기준 (name, email, createdAt 등)
            String direction, // 정렬 방향 (asc, desc)
            String status     // 상태 필터
    ) {

        // 2. 페이지네이션 + 정렬 조건 만들기
        // 사용자는 1페이지부터 생각하지만, Spring Pageable은 0페이지부터 시작하므로 page - 1 해준다.
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.fromString(direction), sortBy)
        );

        // 3. 조건에 따라 조회 결과를 담을 변수 만들기
        // 이번에는 전체 List가 아니라 Page<Customer>로 받는다.
        // 이유: 실제 데이터 목록 + 전체 개수 + 전체 페이지 수를 같이 얻기 위해서
        Page<Customer> customerPage;

        // 경우 1. 검색어 없음 + 상태 없음
        // => 전체 고객 목록 조회
        if ((keyword == null || keyword.isBlank()) && (status == null || status.isBlank())) {
            customerPage = customerRepository.findAll(pageable);

            // 경우 2. 검색어 있음 + 상태 없음
            // => 이름 또는 이메일에 keyword가 포함된 고객 조회
        } else if (keyword != null && !keyword.isBlank() && (status == null || status.isBlank())) {
            customerPage = customerRepository.findByNameContainingOrEmailContaining(keyword, keyword, pageable);

            // 경우 3. 검색어 없음 + 상태 있음
            // => 상태가 일치하는 고객만 조회
        } else if ((keyword == null || keyword.isBlank()) && status != null && !status.isBlank()) {
            customerPage = customerRepository.findByStatus(status, pageable);

            // 경우 4. 검색어 있음 + 상태 있음
            // => 상태도 일치하고, 이름 또는 이메일에 keyword가 포함된 고객 조회
        } else {
            customerPage = customerRepository
                    .findByStatusAndNameContainingOrStatusAndEmailContaining(
                            status, keyword, status, keyword, pageable
                    );
        }

        // 4. 조회된 엔티티 목록만 꺼내서 내부 DTO로 변환하기
        // customerPage.getContent() 는 현재 페이지에 들어있는 고객 목록만 꺼내는 것
        List<GetCustomerResponse> customers = customerPage.getContent().stream()
                .map(customer -> new GetCustomerResponse(
                        customer.getId(),
                        customer.getName(),
                        customer.getEmail(),
                        customer.getPhoneNumber(),
                        customer.getStatus(),
                        customer.getCreatedAt()
                ))
                .toList();

        // 5. 외부 DTO로 감싸서 반환하기
        return new ListCustomerResponse(
                customerPage.getNumber() + 1,      // 현재 페이지 (Spring은 0부터라서 다시 +1)
                customerPage.getSize(),            // 페이지당 개수
                (int) customerPage.getTotalElements(), // 전체 고객 수
                customerPage.getTotalPages(),      // 전체 페이지 수
                customers                          // 현재 페이지의 고객 목록
        );
    }





}
