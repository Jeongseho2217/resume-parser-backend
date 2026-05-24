package com.example.demo.controller;

import com.example.demo.dto.ResumeAnalyzeRequest;
import com.example.demo.dto.ResumeAnalyzeResponse;
import com.example.demo.dto.ResumeSubmitRequest;
import com.example.demo.service.ResumeService;
import com.example.demo.util.FileTextExtractor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

@CrossOrigin(origins = "http://localhost:5173") // vite 주소 허용
@RestController
@RequestMapping("/api/v1/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    private final FileTextExtractor fileTextExtractor;

// 파일을 입력받아 텍스트 추출 후 서비스로 보내기
@PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResumeAnalyzeResponse> analyzeResume(
            @RequestParam("job_id") Long jobId,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("file") MultipartFile file) throws Exception {
        
        // 1. 파일에서 텍스트 추출하기
        String resumeText = fileTextExtractor.extractText(file.getInputStream());
        
        // 2. DTO 만들기
        ResumeAnalyzeRequest request = new ResumeAnalyzeRequest(jobId, name, email, resumeText);
        
        // 3. 기존에 만들어둔 서비스 로직 그대로 호출
        Long resumeId = resumeService.saveResumeAsPending(request);
        resumeService.processAiAnalysisAsync(resumeId, resumeText);
        
        return ResponseEntity.ok(new ResumeAnalyzeResponse(resumeId, "PENDING"));
    }

    // [POST] 이력서 제출 및 저장 API
    @PostMapping("/submit")
    public ResponseEntity<String> submitResume(@RequestBody ResumeSubmitRequest request) {
        // 나중에 GlobalExceptionHandler에서 처리하게 할 예정
        resumeService.saveResumeAndCandidate(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("이력서 제출 및 DB 저장 완료");
    }
}