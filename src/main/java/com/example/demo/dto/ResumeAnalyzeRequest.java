package com.example.demo.dto;

// 클라이언트가 보내는 요청. 자소서를 보내려는 공고 ID와, 이름, 이메일, 자소서 텍스트를 요구
public record ResumeAnalyzeRequest(
    Long job_id,
    String name,
    String email,
    String resume_text
) {}
