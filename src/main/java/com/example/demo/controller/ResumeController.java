package com.example.demo.controller;

import com.example.demo.dto.ResumeAnalyzeRequest;
import com.example.demo.dto.ResumeAnalyzeResponse;
import com.example.demo.dto.ResumeSubmitRequest;
import com.example.demo.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    // [POST] 이력서 분석 요청 API
    @PostMapping("/analyze")
    public ResponseEntity<ResumeAnalyzeResponse> analyzeResume(@RequestBody ResumeAnalyzeRequest request) {
        ResumeAnalyzeResponse response = resumeService.analyzeResume(request);
        return ResponseEntity.ok(response);
    }

    // [POST] 이력서 제출 및 저장 API
    @PostMapping("/submit")
    public ResponseEntity<String> submitResume(@RequestBody ResumeSubmitRequest request) {
        // 나중에 GlobalExceptionHandler에서 처리하게 할 예정
        resumeService.saveResumeAndCandidate(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("이력서 제출 및 DB 저장 완료");
    }
}