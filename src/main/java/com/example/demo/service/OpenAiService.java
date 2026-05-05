package com.example.demo.service;

import com.example.demo.dto.AiAnalyzeResultDto;
import com.example.demo.dto.OpenAiRequest;
import com.example.demo.dto.OpenAiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api-url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiAnalyzeResultDto analyzeWithAI(String resumeText) {
        String prompt = """
    너는 10년 차 IT 전문 채용 담당자야.
    아래 지원자의 이력서를 분석해서, 반드시 주어진 JSON 형식으로만 응답해줘.
    다른 부연 설명이나 인사말은 절대 하지 마.

    [요구사항]
    1. summary: 이력서를 읽고 가장 돋보이는 특징 3가지를 배열로 요약해줘. (예: "~한 경험이 있음", "~를 개발함")
    2. core_competencies: 문제해결, 커뮤니케이션 등 직무와 관련된 핵심 역량 키워드를 배열로 추출해줘.
    3. tech_stacks: 이력서에 언급된 프로그래밍 언어, 프레임워크, 도구 등을 배열로 추출해줘.

    [응답 JSON 형식]
    {
      "summary": ["요약1", "요약2", "요약3"],
      "core_competencies": ["역량1", "역량2"],
      "tech_stacks": ["기술1", "기술2", "기술3"]
    }

    [이력서 내용]
    """ + resumeText;

        OpenAiRequest request = new OpenAiRequest(
                model,
                List.of(new OpenAiRequest.Message("user", prompt))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<OpenAiRequest> entity = new HttpEntity<>(request, headers);
        
        OpenAiResponse response = restTemplate.postForObject(apiUrl, entity, OpenAiResponse.class);

        String aiJsonString = response.getChoices().get(0).getMessage().getContent();
        
        try {
            return objectMapper.readValue(aiJsonString, AiAnalyzeResultDto.class);
        } catch (Exception e) {
            throw new RuntimeException("AI 응답을 파싱하는데 실패했습니다.", e);
        }
    }
}