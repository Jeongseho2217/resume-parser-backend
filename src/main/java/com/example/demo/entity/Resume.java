package com.example.demo.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//지원자 이력서 엔티티
@Entity
@Table(name = "resumes")
@NoArgsConstructor
public class Resume {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
    
    @Column(name = "resume_text", nullable = false, columnDefinition = "TEXT")
    private String resumeText; // 자소서 원문 텍스트 직접 저장 (파일 URL 방식 미사용)

    @Enumerated(EnumType.STRING) 
    @Column(name = "status", length = 20)
    private ResumeStatus status; // AI 분석 상태 (PENDING / DONE / FAILED)

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
    
    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public void setJobPosting(JobPosting jobPosting) {
        this.jobPosting = jobPosting;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResumeText() {
		return resumeText;
	}

	public void setResumeText(String resumeText) {
		this.resumeText = resumeText;
	}

	public ResumeStatus getStatus() {
		return status;
	}

	public void setStatus(ResumeStatus status) {
		this.status = status;
	}

	public RecruitmentStatus getRecruitmentStatus() {
		return recruitmentStatus;
	}

	public void setRecruitmentStatus(RecruitmentStatus recruitmentStatus) {
		this.recruitmentStatus = recruitmentStatus;
	}

	public LocalDateTime getAppliedAt() {
		return appliedAt;
	}

	public void setAppliedAt(LocalDateTime appliedAt) {
		this.appliedAt = appliedAt;
	}

	public Recruiter getRecruiter() {
		return recruiter;
	}

	public void setRecruiter(Recruiter recruiter) {
		this.recruiter = recruiter;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public JobPosting getJobPosting() {
		return jobPosting;
	}
}

