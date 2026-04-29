package com.example.customermanagementprojectteam11.order.service;


import com.example.customermanagementprojectteam11.order.entity.Order;
import com.example.customermanagementprojectteam11.order.entity.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {
    // Specification은 JPA가 WHERE 조건문을 만들 때 사용하는 객체
    // return 안에서 실제 SQL 조건식을 생성해서 넘겨줌
    // root: 쿼리의 루트 / query: CriteriaQuery 객체 / criteriaBuilder: 조건 생성기
    // 주문번호, 주문자명 검색조건으로 만들고 입력한 keyword가 포함된 데이터를 조회
    public static Specification<Order> keyword(String keyword) {
        return (root, query, criteriaBuilder) ->
                // Order 엔티티의 orderNumber, customerName 컬럼 조회하고 keyword 포함 여부 검색
                criteriaBuilder.or(criteriaBuilder.like(root.get("orderNumber"), "%" + keyword + "%"),
                        criteriaBuilder.like(root.get("customerName"), "%" + keyword + "%")
                );
    }
     // 상태 필터
    public static Specification<Order> status(OrderStatus status) {
        // 요청받은 status값과 같은 데이터만 조회
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }
}
