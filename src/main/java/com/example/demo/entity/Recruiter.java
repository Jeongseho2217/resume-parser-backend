package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//인사 담당자 엔티티
@Entity
@Table(name = "recruiters")
@Getter
@Setter
@NoArgsConstructor
public class Recruiter {

    @Id
    private String id; 

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password; // 실제 서비스에서는 암호화된 해시값 저장 필요
 
    @Column(name = "company_name", length = 50)
    private String companyName;
}