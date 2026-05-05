package com.example.demo.dto;

import java.util.List;

// 지원자 한 명의 요약 정보를 담는 DTO / 이력서 ID, 지원자명, 검토 상태, 요약상태, 매칭도, 직무, 기술 태그
public record CandidateSummary(
    Long resume_id,
    String candidate_name,
    String status,
    String recruitment_status,
    int matching_score,
    List<String> technical_skills,
    List<String> core_competencies
) {}

// Candidate에서 사용하는 DTO