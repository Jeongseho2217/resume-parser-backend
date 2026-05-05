package com.example.demo.dto;

// 페이징 정보를 담는 공통 DTO / 현재 페이지, 페이지당 공고 개수, 총 페이지, 페이지 카운트
public record PageInfo(
    int current_page,
    int page_size,
    int total_pages,
    int total_count
) {}

// Candidate에서 사용하는 DTO