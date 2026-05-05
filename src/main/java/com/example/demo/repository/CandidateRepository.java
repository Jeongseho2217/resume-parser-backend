package com.example.demo.repository;

import com.example.demo.entity.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    // 1. 특정 채용 공고에 지원한 지원자 목록 조회
    Page<Candidate> findByJobPostingId(Long jobPostingId, Pageable pageable);

    // 2. 해시태그 필터링을 포함한 지원자 조회
    // 해시태그 기반 필터링을 처리하기 위한 쿼리.
    @Query(value = "SELECT c.* FROM candidates c " +
            "JOIN resumes r ON c.id = r.candidate_id " +
            "JOIN analysis_results a ON r.id = a.resume_id " +
            "WHERE c.job_posting_id = :jobId " +
            "AND (:hashtag IS NULL OR a.technical_skills::text LIKE CONCAT('%', :hashtag, '%'))", 
            nativeQuery = true)
    Page<Candidate> findCandidatesWithFilter(
        @Param("jobId") Long jobId, 
        @Param("hashtag") String hashtag, 
        Pageable pageable
    );

    // 3. 이메일로 기존 지원자 찾기
    // 이력서 제출 시 기존에 등록된 지원자인지 확인할 때 사용.
    Optional<Candidate> findByEmail(String email);
}