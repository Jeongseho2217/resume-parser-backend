package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//채용 공고 엔티티
@Entity
@Table(name = "job_postings")
@NoArgsConstructor
public class JobPosting {

    @Id
    private String id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "requirement", columnDefinition = "TEXT")
    private String requirement; // 공고 내용 및 우대사항

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	public Recruiter getRecruiter() {
		return recruiter;
	}

	public void setRecruiter(Recruiter recruiter) {
		this.recruiter = recruiter;
	}
}