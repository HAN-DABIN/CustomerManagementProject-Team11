package com.example.customermanagementprojectteam11.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    // 속성
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt; // 관리자 생성일
    @Column(name = "approved_at")
    private LocalDateTime approvedAt; // 관리자 승인일
    @LastModifiedDate
    private LocalDateTime modifiedAt; // 관리자 정보 수정일
    private LocalDateTime rejectedAt; // 거부 일자
}
