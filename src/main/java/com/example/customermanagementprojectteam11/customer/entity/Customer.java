package com.example.customermanagementprojectteam11.customer.entity;

import com.example.customermanagementprojectteam11.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table (name = "customers")
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //고객명 VARCHAR(50)
    @Column(length = 50, nullable = false)
    private String name;

    //고객 이메일 VARCHAR(200)
    @Column(length = 200, nullable = false, unique = true)
    private String email;

    //고객 전화번호 VARCHAR(20)
    @Column(name = "phone_number", length = 20, nullable = false)
    private String phoneNumber;

    //고객 상태 VARCHAR(20) 기본값 = NORMAL
    @Column(length = 20, nullable = false)
    private String status;


    public Customer(String email, String name, String phoneNumber, String status) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }
}
