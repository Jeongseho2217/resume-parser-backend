package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CandidateListResponse;
import com.example.demo.dto.CandidateDetailResponse;
import com.example.demo.service.CandidateService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    // =====================================================================
    // [API 3] 대시보드 지원자 목록 조회
    // URL 예시: /api/v1/candidates?job_id=1&page=1&page_size=10
    // =====================================================================
    @GetMapping
    public ResponseEntity<CandidateListResponse> getCandidateList(
            @RequestParam(name = "job_id") Long jobId, 
            @RequestParam(name = "hashtag", required = false) String hashtag, 
            @RequestParam(name = "page") int page, 
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize, 
            @RequestParam(name = "sort", required = false, defaultValue = "match_score_desc") String sort) { 
        CandidateListResponse response = candidateService.getCandidates(jobId, page, pageSize);
        return ResponseEntity.ok(response);
    }

    // =====================================================================
    // [API 4] 특정 지원자 이력서 상세 조회 (프론트 로딩 스피너 및 모달창 용도)
    // URL 예시: /api/v1/candidates/15
    // =====================================================================
    @GetMapping("/{resume_id}")
    public ResponseEntity<CandidateDetailResponse> getCandidateDetail(
            @PathVariable("resume_id") Long resumeId) { // 경로에 있는 숫자를 변수로 받아옴
        
        CandidateDetailResponse response = candidateService.getCandidateDetail(resumeId);
        return ResponseEntity.ok(response);
    }
}