package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 채용 공고 엔티티
@Entity
@Table(name = "job_postings")
@NoArgsConstructor
@Getter // getTitle(), getId() 등 자동 제작
@Setter // setTitle(), setId() 등 자동 제작
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "requirement", columnDefinition = "TEXT")
    private String requirement; // 공고 내용 및 우대사항

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;
}