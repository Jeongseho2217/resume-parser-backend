package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page; //Spring Data에서 페이징 결과를 담는 Page<T> 타입을 사용하기 위함
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CandidateListResponse; // com/example/demo/dto/CandidateListResponse 참조
import com.example.demo.dto.CandidateSummary; // com/example/demo/dto/CandidateSummary 참조
import com.example.demo.dto.PageInfo; // com/example/demo/dto/PageInfo 참조

import com.example.demo.repository.CandidateRepository; // com/example/demo/repository/CandidateRepository 참조

import com.example.demo.entity.Candidate; // com/example/demo/entity/Candidate 참조

import lombok.RequiredArgsConstructor; // @RequiredArgsConstructor에 필요

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateListResponse getCandidates(Long jobId, int page, int pageSize) {
        // [1] 페이징 설정 (0부터 시작하므로 page - 1 처리)
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());

        // [2] DB 조회 (Page 객체로 반환)
        Page<Candidate> candidatePage = candidateRepository.findByJobPostingId(jobId, pageable);

        // [3] Entity 리스트 -> CandidateSummary DTO 리스트로 변환
        List<CandidateSummary> candidateSummaries = candidatePage.getContent().stream()
            .map(candidate -> new CandidateSummary(
                candidate.getResume().getId(),      // resume_id
                candidate.getName(),    // candidate_name[cite: 1]
                candidate.getResume().getStatus().toString(), // status (PENDING/DONE/FAILED)[cite: 1]
                candidate.getRecruitmentStatus(),   // recruitment_status[cite: 1]
                //candidate.getResume().getMatchingScore() // matching_score[cite: 1] // AI 매칭도,, 일단 임시 주석 처리
                0, // 0으로 임시값
                // 기술 태그와 역량은 분석 결과에서 가져오기
                List.of("#Spring_Boot", "#MySQL"),   // technical_skills[cite: 1]
                List.of("#문제해결능력")      // core_competencies[cite: 1]
            ))
            .toList();

        // [4] Page 객체의 정보를 PageInfo DTO로 변환
        PageInfo pageInfo = new PageInfo(
            candidatePage.getNumber() + 1,        // current_page (다시 1부터 시작하게 보정)[cite: 1]
            candidatePage.getSize(),              // page_size[cite: 1]
            candidatePage.getTotalPages(),        // total_pages[cite: 1]
            (int) candidatePage.getTotalElements() // total_count[cite: 1]
        );

        // [5] 최종 응답 객체 생성 및 반환
        return new CandidateListResponse(pageInfo, candidateSummaries);
    }
}