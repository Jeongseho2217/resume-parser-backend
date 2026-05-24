package com.example.demo.entity;

import java.time.LocalDateTime;

import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 이력서 엔티티
@Entity
@Table(name = "resumes")
@NoArgsConstructor
@Getter
@Setter
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB 자동 증가 설정
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
    
    @Column(name = "resume_text", nullable = false, columnDefinition = "TEXT")
    private String resumeText; // 자소서 원문 텍스트 직접 저장

    @Enumerated(EnumType.STRING) 
    @Column(name = "status", length = 20)
    private ResumeStatus status; // AI 분석 상태 (PENDING / DONE / FAILED)

    @Column(name = "experience", length = 100)
    private String experience; // 경력

    @Column(name = "school", length = 100)
    private String school; // 학력

    @Enumerated(EnumType.STRING)
    @Column(name = "recruitment_status", length = 20)
    private RecruitmentStatus recruitmentStatus;  // 전형 상태 (검토중 / 합격 / 불합격)

    @Column(name = "applied_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime appliedAt; // 지원 일자, 최초 생성 시 자동 입력
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id")
    private JobPosting jobPosting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;

    @OneToOne(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AnalysisResult analysisResult;

    // ==========================================================
    // AI 분석 결과를 안전하게 꺼내오기 위한 편의 메서드 (Helper Methods)
    // 분석 전(PENDING)이라서 analysisResult가 null일 경우 에러가 나지 않도록 방어.
    // ==========================================================

    public Integer getMatchingScore() {
        if (this.analysisResult == null) return 0;
        return this.analysisResult.getMatchingScore(); 
    }

    public List<String> getTechSkillsList() {
        if (this.analysisResult == null) return List.of();
        return this.analysisResult.getTechnicalSkills();
    }

    public List<String> getCoreCompetenciesList() {
        if (this.analysisResult == null) return List.of();
        return this.analysisResult.getCoreCompetencies();
    }

    public List<String> getSummaryList() {
        if (this.analysisResult == null) return List.of();
        return this.analysisResult.getSummary();
    }

    // 날짜를 가져올 때 getCreatedAt() 대신 사용하도록 일치시킵니다.
    public LocalDateTime getCreatedAt() {
        return this.appliedAt;
    }
}