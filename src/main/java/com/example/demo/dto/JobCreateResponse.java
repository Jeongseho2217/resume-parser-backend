package com.example.demo.dto;

// 서버가 돌려주는 응답 데이터. 공고 아이디를 생성하고 메시지를 출력 
public record JobCreateResponse(
    Long job_id, 
    String message
) {}

// JobPosting에서 사용하는 DTO