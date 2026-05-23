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

    // [POST] 비동기 방식의 이력서 분석 요청 API
    @PostMapping("/analyze")
    public ResponseEntity<ResumeAnalyzeResponse> analyzeResume(@RequestBody ResumeAnalyzeRequest request) {
        
        //AI에게 텍스트를 주기전에, 일단 DB에 'PENDING' 상태로 저장.
        Long resumeId = resumeService.saveResumeAsPending(request);
        
        //응답 속도가 느린 AI 분석 작업은 백그라운드 스레드에서 처리.
        // 컨트롤러가 ResumeService의 @Async 메서드를 직접 호출하므로 비동기로 작동.
        resumeService.processAiAnalysisAsync(resumeId, request.resume_text());
        
        //AI 결과만 기다리지 않고, 그 사이에 방금 발급된 이력서 ID와 대기 상태를 프론트엔드에 즉시 반환.
        ResumeAnalyzeResponse response = new ResumeAnalyzeResponse(resumeId, "PENDING");
        
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