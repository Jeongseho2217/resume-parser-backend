package com.example.demo.controller;

import com.example.demo.dto.ResumeAnalyzeResponse;
import com.example.demo.dto.ResumeSubmitRequest;
import com.example.demo.service.ResumeService;

// 백업용으로 남겨둔 import
import com.example.demo.util.FileTextExtractor;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173") // vite 프론트엔드 주소 허용
@RestController
@RequestMapping("/api/v1/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    private final FileTextExtractor fileTextExtractor; // 백업용 의존성

    // ==========================================
    // 피드백 반영 (3분할 폼 기반 비동기 통합 API)
    // ==========================================
    @PostMapping("/submit")
    public ResponseEntity<ResumeAnalyzeResponse> submitAndAnalyzeResume(@RequestBody ResumeSubmitRequest request) {
        
        // 1. DB에 3분할 데이터 저장 후 PENDING 상태 반환
        Long resumeId = resumeService.saveResumeAsPending(request);
        
        // 2. 백그라운드 LLaMA AI 분석 시작
        resumeService.processAiAnalysisAsync(resumeId);
        
        // 3. 즉시 응답
        return ResponseEntity.ok(new ResumeAnalyzeResponse(resumeId, "PENDING"));
    }

    // ==========================================
    // ★ 백업: 기존 PDF 파일 업로드 기반 텍스트 추출 API ★
    // 프론트엔드에서 파일 업로드 기능을 그대로 사용할 경우를 대비해 봉인(주석 처리)해 둡니다.
    // 필요 시 주석만 해제하고 서비스 로직을 살짝 맞춰서 부활시킬 수 있습니다.
    // ==========================================
    /*
    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResumeAnalyzeResponse> analyzeResume(
            @RequestParam("job_id") Long jobId,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("file") MultipartFile file) throws Exception {
        
        // 1. 파일에서 텍스트 추출하기
        String resumeText = fileTextExtractor.extractText(file.getInputStream());
        
        // (주의: 부활시킬 경우 현재 3분할 DTO 규격에 맞게 데이터를 세팅해주는 추가 로직이 필요함)
        // ResumeAnalyzeRequest request = new ResumeAnalyzeRequest(jobId, name, email, resumeText);
        // Long resumeId = resumeService.saveResumeAsPending(request);
        // resumeService.processAiAnalysisAsync(resumeId, resumeText);
        
        return ResponseEntity.ok(new ResumeAnalyzeResponse(999L, "PENDING")); // 임시 리턴
    }
    */
}