package com.example.demo.service;

import com.example.demo.dto.JobCreateRequest;
import com.example.demo.dto.JobCreateResponse;
import com.example.demo.entity.JobPosting;
import com.example.demo.repository.JobPostingRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobPostingRepository jobPostingRepository;

    @Transactional // DB 반영을 보장
    public JobCreateResponse createJob(JobCreateRequest request) {
        // 실제 엔티티를 생성하여 저장.

        JobPosting jobPosting = new JobPosting();
        jobPosting.setTitle(request.title());
        jobPosting.setRequirement(request.requirement());
        
        // recruiter_id는 현재 시스템 구조에 맞게 세팅
        // jobPosting.setRecruiter(...); 

        // 실제 DB 저장 호출
        JobPosting savedJob = jobPostingRepository.save(jobPosting);

        // DB가 생성해준 실제 ID를 반환.
        return new JobCreateResponse(savedJob.getId(), "채용 공고가 등록되었습니다.");
    }
}