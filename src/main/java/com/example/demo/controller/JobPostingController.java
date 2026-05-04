package com.example.demo.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/jobs")
public class JobPostingController {

    // [POST] /api/v1/jobs
    @PostMapping
    public Map<String, Object> createJob(@RequestBody Map<String, Object> request) {
        // 프론트엔드에서 넘어오는 데이터(recruiter_id, title, requirement)를 받습니다.
        // 향후 이 부분에 DB INSERT 로직이 들어갑니다.
        
        // 1. 임의의 job_id 생성 (DB 자동 증가 키 또는 UUID 대체)
        String generatedJobId = "job_" + UUID.randomUUID().toString().substring(0, 5);

        // 2. 노션 명세서와 동일한 형태의 응답 데이터 반환
        return Map.of(
                "job_id", generatedJobId,
                "message", "채용 공고가 등록되었습니다."
        );
    }
}