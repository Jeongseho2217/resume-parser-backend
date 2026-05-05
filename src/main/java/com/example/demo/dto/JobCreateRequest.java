package com.example.demo.dto;

// 클라이언트가 보내는 요청 데이터, 채용 담당자 id, 공고 제목, 공고 내용을 포함하여야 함
public record JobCreateRequest( 
    String recruiter_id,
    String title,
    String requirement
) {}

// JobPosting에서 사용하는 DTO