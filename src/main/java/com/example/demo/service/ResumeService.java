package com.example.demo.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ResumeSubmitRequestDto;
import com.example.demo.entity.Candidate;
import com.example.demo.entity.JobPosting;
import com.example.demo.entity.MemberType;
import com.example.demo.entity.Resume;
import com.example.demo.entity.ResumeStatus;
import com.example.demo.repository.CandidateRepository;
import com.example.demo.repository.ResumeRepository;

@Service
public class ResumeService {

    private final CandidateRepository candidateRepository;
    private final ResumeRepository resumeRepository;

    public ResumeService(CandidateRepository candidateRepository, ResumeRepository resumeRepository) {
        this.candidateRepository = candidateRepository;
        this.resumeRepository = resumeRepository;
    }

    @Transactional
    public void saveResumeAndCandidate(ResumeSubmitRequestDto dto) {
        // 1. 지원자(Candidate) 정보 세팅
        Candidate candidate = new Candidate();
        candidate.setId("c_" + UUID.randomUUID().toString().substring(0, 8)); 
        candidate.setName(dto.getCandidate_name());
        candidate.setEmail(dto.getEmail());
        candidate.setPassword(dto.getPassword());
        
        // Enum에 존재하는 MEMBER 값으로 주입
        candidate.setMemberType(MemberType.MEMBER); 

        Candidate savedCandidate = candidateRepository.save(candidate);

        // 2. 이력서(Resume) 정보 세팅
        Resume resume = new Resume();
        resume.setId("r_" + UUID.randomUUID().toString().substring(0, 8));
        resume.setCandidate(savedCandidate); 
        resume.setResumeText(dto.getResume_text());
        
        // Enum 값으로 PENDING 주입
        resume.setStatus(ResumeStatus.PENDING);

        // 3. 공고(JobPosting) 매핑
        JobPosting jobPosting = new JobPosting();
        
        jobPosting.setId(String.valueOf(dto.getJob_posting_id())); 
        resume.setJobPosting(jobPosting);

        resumeRepository.save(resume);
    }
}