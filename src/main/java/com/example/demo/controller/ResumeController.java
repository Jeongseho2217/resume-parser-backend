package com.example.demo.controller;

import com.example.demo.dto.ResumeSubmitRequestDto;
import com.example.demo.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    // 생성자 (서비스 연결)
    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/analyze")
    public Map<String, Object> analyzeResume(@RequestBody Map<String, Object> request) {
        String generatedResumeId = "r_" + UUID.randomUUID().toString().substring(0, 5);
        return Map.of(
                "resume_id", generatedResumeId,
                "status", "PENDING"
        );
    }

    // 이력서 제출용 코드 추가
    @PostMapping("/submit")
    public ResponseEntity<String> submitResume(@RequestBody ResumeSubmitRequestDto request) {
        try {
            // Service를 호출해서 DB에 저장
            resumeService.saveResumeAndCandidate(request);
            return ResponseEntity.ok("이력서 제출 및 DB 저장 완료");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 저장 중 오류 발생");
        }
    }
}