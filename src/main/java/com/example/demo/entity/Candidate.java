package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "candidates")
@Getter
@Setter
@NoArgsConstructor // JPA 기본 생성자
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // candidate_name

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type", length = 20, nullable = false)
    private MemberType memberType;

    // 명세서 5번 API: 전형 상태 (기본값 "검토중")
    @Column(name = "recruitment_status", nullable = false)
    private String recruitmentStatus = "검토중";

    // 어떤 공고에 지원했는지 (1:n 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id")
    private JobPosting jobPosting;

    // 지원자의 이력서 (1:1 관계)
    // Resume 엔티티에 있는 candidate 필드와 연결
    @OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Resume resume;

    // 처음 지원자를 생성할 때 사용하는 빌더 또는 생성자
    public Candidate(String name, String email, String password, JobPosting jobPosting) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.jobPosting = jobPosting;
    }

    // 상태 변경 메서드 (명세서 5번 API 용)
    public void updateRecruitmentStatus(String status) {
        this.recruitmentStatus = status;
    }
}