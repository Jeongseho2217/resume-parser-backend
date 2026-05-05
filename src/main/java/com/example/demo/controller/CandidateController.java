package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CandidateListResponse; // /com/example/demo/dto/CandidateListResponse 참조
import com.example.demo.service.CandidateService; // /com/example/demo/service/CandidateService 참조

import lombok.RequiredArgsConstructor; // @RequiredArgsConstructor 위해 필요

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
public class CandidateController {

        private final CandidateService candidateService;

	@GetMapping
        public ResponseEntity<CandidateListResponse> getCandidateList( // api 명세서에 표기된 대로 리스트화
            @RequestParam(name = "job_id") Long jobId, // 공고 ID. 꼭 필요하며 공고를 구분하는데 사용. 속도 향상 및 편의성을 이유로 Long타입으로 변경
            @RequestParam(name = "hashtag", required = false) String hashtag, // 해시 태그 꼭 필요하지는 않지만 
            @RequestParam(name = "page") int page, // 필수
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize, // 한 페이지당 공고를 몇 개 나타낼건지? 기본값은 10개
            @RequestParam(name = "sort", required = false, defaultValue = "match_score_desc") String sort) { // 정렬 기준. 
        CandidateListResponse response = candidateService.getCandidates(jobId, page, pageSize);
        return ResponseEntity.ok(response);
    }
}