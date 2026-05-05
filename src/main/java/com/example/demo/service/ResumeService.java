package com.example.demo.service;

import com.example.demo.dto.ResumeAnalyzeRequest;
import com.example.demo.dto.ResumeAnalyzeResponse;
import com.example.demo.dto.ResumeSubmitRequestDto;
import com.example.demo.entity.Candidate;
import com.example.demo.entity.JobPosting;
import com.example.demo.entity.MemberType;
import com.example.demo.entity.Resume;
import com.example.demo.entity.ResumeStatus;
import com.example.demo.repository.CandidateRepository;
import com.example.demo.repository.JobPostingRepository;
import com.example.demo.repository.ResumeRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final CandidateRepository candidateRepository;
    private final ResumeRepository resumeRepository;
    private final JobPostingRepository jobPostingRepository;

    @Transactional
    public void saveResumeAndCandidate(ResumeSubmitRequestDto dto) {
        
        // DB에서 실제 존재하는 공고 엔티티를 조회
        JobPosting jobPosting = jobPostingRepository.findById(dto.getJob_posting_id())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공고 ID입니다: " + dto.getJob_posting_id()));

        // 지원자 정보 세팅
        Candidate candidate = new Candidate();
        candidate.setName(dto.getCandidate_name());
        candidate.setEmail(dto.getEmail());
        candidate.setPassword(dto.getPassword());
        candidate.setMemberType(MemberType.MEMBER); 
        candidate.setJobPosting(jobPosting); // 💡 찾아온 진짜 엔티티를 넣어줍니다.

        Candidate savedCandidate = candidateRepository.save(candidate);

        // 이력서 정보 세팅
        Resume resume = new Resume();
        resume.setCandidate(savedCandidate); 
        resume.setResumeText(dto.getResume_text());
        resume.setStatus(ResumeStatus.PENDING);

        resumeRepository.save(resume);
    }

    public ResumeAnalyzeResponse analyzeResume(ResumeAnalyzeRequest request) {
        return new ResumeAnalyzeResponse(1L, "PENDING");
    }
}