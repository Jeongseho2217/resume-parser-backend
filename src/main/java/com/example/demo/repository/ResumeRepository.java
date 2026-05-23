package com.example.demo.repository;

import com.example.demo.entity.Resume;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

// ResumeRepository - ResumeService에서 요청하면 저장된 이력서를 빼오는 레포지토리(창고) 역할

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    // 모든 이력서를 최신순으로 페이징해서 가져오는 기본 쿼리
    Page<Resume> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    // 만약 'PENDING'이나 'DONE' 상태만 필터링하고 싶을 경우 활성화
    // Page<Resume> findByStatusOrderByCreatedAtDesc(ResumeStatus status, Pageable pageable);
}