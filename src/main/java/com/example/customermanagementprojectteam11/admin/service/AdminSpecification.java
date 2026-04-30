package com.example.customermanagementprojectteam11.admin.service;

import com.example.customermanagementprojectteam11.admin.entity.Admin;
import com.example.customermanagementprojectteam11.admin.entity.AdminRole;
import com.example.customermanagementprojectteam11.admin.entity.AdminStatus;
import org.springframework.data.jpa.domain.Specification;

public class AdminSpecification {
    // Specification은 JPA가 WHERE 조건문을 만들 때 사용하는 객체
    // return 안에서 실제 SQL 조건식을 생성해서 넘겨줌
    // root: 쿼리의 루트 / query: CriteriaQuery 객체 / criteriaBuilder: 조건 생성기
    // 이름, 이메일 검색조건으로 만들고 입력한 keyword가 포함된 데이터를 조회
    public static Specification<Admin> keyword(String keyword) {
        return (root, query, criteriaBuilder) ->
                // Admin 엔티티의 name, email 컬럼 조회하고 keyword 포함 여부 검색
                criteriaBuilder.or(criteriaBuilder.like(root.get("name"), "%" + keyword + "%"),
                        criteriaBuilder.like(root.get("email"), "%" + keyword + "%")
                );
    }
    // 역할 필터 조건
    public static Specification<Admin> role(AdminRole role) {
        // 요청받은 role값과 같은 데이터만 조회
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("role"), role);
    } // 상태 필터
    public static Specification<Admin> status(AdminStatus status) {
        // 요청받은 status값과 같은 데이터만 조회
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }
}


