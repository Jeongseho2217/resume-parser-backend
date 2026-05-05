package com.example.demo.controller;

import com.example.demo.dto.ResumeAnalyzeRequest;
import com.example.demo.dto.ResumeAnalyzeResponse;
import com.example.demo.dto.ResumeSubmitRequestDto;
import com.example.demo.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/resumes")
@RequiredArgsConstructor // 💡 생성자 코드를 대신 만들어주는 마법의 어노테이션!
public class ResumeController {

    private final ResumeService resumeService;

    // [POST] 이력서 분석 요청 API
    @PostMapping("/analyze")
    public ResumeAnalyzeResponse analyzeResume(@RequestBody ResumeAnalyzeRequest request) {
        // 실제 분석 로직은 Service에게 맡깁니다.
        return resumeService.analyzeResume(request);
    }

    // [POST] 이력서 제출 및 저장 API (기존 코드 유지)
    @PostMapping("/submit")
    public ResponseEntity<String> submitResume(@RequestBody ResumeSubmitRequestDto request) {
        try {
            resumeService.saveResumeAndCandidate(request);
            return ResponseEntity.ok("이력서 제출 및 DB 저장 완료");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 저장 중 오류 발생");
        }
    }
}