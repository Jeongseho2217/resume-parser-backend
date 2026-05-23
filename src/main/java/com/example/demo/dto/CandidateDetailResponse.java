package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// CandidateDetailResponse은 면접관이 특정 지원자 한 명을 클릭했을 때 보는 '상세 프로필(이력서 원본)'을 저장하는 DTO입니다.

@JsonInclude(JsonInclude.Include.ALWAYS) 
public record CandidateDetailResponse(
    String status,
    
    @JsonProperty("applied_at")
    String appliedAt, // ISO-8601 문자열 포맷
    
    @JsonProperty("analysis_result")
    AnalysisResult analysisResult
) {
    // 내부 객체: AI 분석 결과 알맹이
    public record AnalysisResult(
        List<String> summary,
        
        @JsonProperty("technical_skills")
        List<String> technicalSkills,
        
        @JsonProperty("core_competencies")
        List<String> coreCompetencies,
        
        @JsonProperty("matching_score")
        Integer matchingScore,
        
        String content // 자소서 원문
    ) {}
}