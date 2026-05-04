package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/candidates")
public class CandidateController {

	@GetMapping
    public Map<String, Object> getCandidateList(
            @RequestParam(name = "job_id") String jobId, // 명세서 기준 필수(required=true가 기본값)
            @RequestParam(name = "hashtag", required = false) String hashtag, // 선택
            @RequestParam(name = "page") int page, // 명세서 기준 필수
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "sort", required = false, defaultValue = "match_score_desc") String sort) {
    	
        // 1. 노션 명세서와 동일한 page_info 세팅
        Map<String, Object> pageInfo = Map.of(
                "current_page", page,
                "page_size", 10,
                "total_pages", 5,
                "total_count", 42
        );

        // 2. 노션 명세서와 동일한 candidates 배열 세팅
        List<Map<String, Object>> candidates = List.of(
                Map.of(
                        "resume_id", "r_12345",
                        "candidate_name", "홍길동",
                        "status", "DONE",
                        "recruitment_status", "검토중",
                        "matching_score", 88,
                        "technical_skills", List.of("#Spring_Boot", "#MySQL", "#REST_API"),
                        "core_competencies", List.of("#문제해결능력", "#팀워크")
                ),
                Map.of(
                        "resume_id", "r_12346",
                        "candidate_name", "김철수",
                        "status", "DONE",
                        "recruitment_status", "서류합격",
                        "matching_score", 95,
                        "technical_skills", List.of("#React", "#TypeScript", "#AWS"),
                        "core_competencies", List.of("#리더십", "#커뮤니케이션")
                )
        );

        // 3. 최종 응답 조립
        return Map.of(
                "page_info", pageInfo,
                "candidates", candidates
        );
    }
}