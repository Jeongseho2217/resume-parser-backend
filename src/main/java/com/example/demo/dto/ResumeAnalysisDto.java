package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// AI가 반환하는 이력서 분석 결과를 받기 위한 전용 DTO입니다.
// Spring Boot <-> LM Studio 끼리 소통하기 위한 DTO

public record ResumeAnalysisDto(
    List<String> summary, // 3줄 본석된 요약 결과를 받음
    
    @JsonProperty("tech_stacks")
    List<String> techStacks, // 반환된 기술 스택 태그들
    
    @JsonProperty("core_competencies")
    List<String> coreCompetencies // 반환된 핵심 역량 태그들
) {}