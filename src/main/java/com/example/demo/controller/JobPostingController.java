package com.example.demo.controller;

import com.example.demo.dto.JobCreateRequest;
import com.example.demo.dto.JobCreateResponse;
import com.example.demo.service.JobService;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor; // @RequiredArgsConstructor 위해 필요

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobService jobService;

    // [POST] /api/v1/jobs
    @PostMapping
    public JobCreateResponse createJob(@RequestBody JobCreateRequest request) {
        // 실제 로직은 Service에서 처리, 결과만 받아오기.
        return jobService.createJob(request);
    }
}