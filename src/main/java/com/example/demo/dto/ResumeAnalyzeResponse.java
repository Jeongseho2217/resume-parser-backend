package com.example.demo.dto;

// 서버가 돌려주는 응답 데이터. 자소서 ID를 생성하고 현재 상태를 반환 
public record ResumeAnalyzeResponse(
    Long resume_id,
    String status // PENDING 둥..
) {}