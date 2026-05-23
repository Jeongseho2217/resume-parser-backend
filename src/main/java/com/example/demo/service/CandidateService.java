package com.example.demo.service;

import java.util.List;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CandidateListResponse;
import com.example.demo.dto.CandidateSummary;
import com.example.demo.dto.PageInfo;
import com.example.demo.dto.CandidateDetailResponse;
import com.example.demo.repository.CandidateRepository;
import com.example.demo.repository.ResumeRepository;

import com.example.demo.entity.Candidate;
import com.example.demo.entity.Resume;
import com.example.demo.entity.ResumeStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ResumeRepository resumeRepository;

    // ================================
    // [API 3] 대시보드 지원자 목록 조회
    // ================================
    @Transactional(readOnly = true)
    public CandidateListResponse getCandidates(Long jobId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());
        Page<Candidate> candidatePage = candidateRepository.findByJobPostingId(jobId, pageable);

        List<CandidateSummary> candidateSummaries = candidatePage.getContent().stream()
            .map(candidate -> {
                Resume resume = candidate.getResume(); // 이력서 정보 가져오기
                
                return new CandidateSummary(
                    resume.getId(),
                    candidate.getName(),
                    resume.getStatus().name(),
                    candidate.getRecruitmentStatus(),
                    
                    resume.getMatchingScore() != null ? resume.getMatchingScore() : 0,
                    resume.getTechSkillsList() != null ? resume.getTechSkillsList() : List.of(),
                    resume.getCoreCompetenciesList() != null ? resume.getCoreCompetenciesList() : List.of()
                );
            })
            .toList();

        PageInfo pageInfo = new PageInfo(
            candidatePage.getNumber() + 1,
            candidatePage.getSize(),
            candidatePage.getTotalPages(),
            (int) candidatePage.getTotalElements()
        );

        return new CandidateListResponse(pageInfo, candidateSummaries);
    }

    // ==================================
    // [API 4] 특정 지원자 이력서 상세 조회
    // ==================================
    @Transactional(readOnly = true)
    public CandidateDetailResponse getCandidateDetail(Long resumeId) {
        
        // 1. DB에서 이력서 조회
        Resume resume = resumeRepository.findById(resumeId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이력서입니다.")); // 예외처리

        // 2. 상태가 PENDING 또는 FAILED일 때 로딩 스피너 돌리기
        if (resume.getStatus() == ResumeStatus.PENDING || resume.getStatus() == ResumeStatus.FAILED) {
            return new CandidateDetailResponse(
                resume.getStatus().name(), 
                null, 
                null  
            );
        }

        // 3. 상태가 DONE일 때의 응답 조립
        CandidateDetailResponse.AnalysisResult resultDto = new CandidateDetailResponse.AnalysisResult(
            resume.getSummaryList(), 
            resume.getTechSkillsList(), 
            resume.getCoreCompetenciesList(), 
            resume.getMatchingScore(), 
            resume.getResumeText() 
        );

        // 등록일자 ISO 포맷 변환
        String appliedAtIso = resume.getCreatedAt() != null ? 
            resume.getCreatedAt().toString() : null;

        return new CandidateDetailResponse("DONE", appliedAtIso, resultDto);
    }
}