package com.example.demo.dto;

import java.util.List;

// 최종 응답 DTO // 페이지 정보, 지원자 목록
public record CandidateListResponse(
    PageInfo page_info,
    List<CandidateSummary> candidates
) {}

// Candidate에서 사용하는 DTO